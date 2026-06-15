package com.crypto.data.repository

import com.crypto.core.logging.Logger
import com.crypto.data.local.room.TradeDao
import com.crypto.data.mapper.toDomain
import com.crypto.data.mapper.toEntity
import com.crypto.data.remote.api.BinanceApi
import com.crypto.data.remote.datasource.TradeWebSocketDataSource
import com.crypto.domain.model.Trade
import com.crypto.domain.repository.TradeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of TradeRepository
 *
 * Strategy:
 * Load recent trades from REST API → Store in DB
 * Connect WebSocket for new trades → Insert into DB
 * Observe DB changes → Emit to UI
 */
@Singleton
class TradeRepositoryImpl @Inject constructor(
    private val api: BinanceApi,
    private val wsDataSource: TradeWebSocketDataSource,
    private val dao: TradeDao,
    private val logger: Logger
) : TradeRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val activeStreams = mutableSetOf<String>()

    override suspend fun getRecentTrades(symbol: String, limit: Int): List<Trade> {
        logger.i(message = "Fetching recent trades: $symbol")

        return try {
            val response = api.getRecentTrades(symbol, limit)

            // Convert to entities and store
            val entities = response.map { it.toEntity(symbol) }
            dao.insertTrades(entities)

            logger.d(message = "${entities.size} trades stored for $symbol")

            // Return domain models
            response.map { it.toDomain(symbol) }
        } catch (e: Exception) {
            logger.e(message = "Failed to fetch trades: ${e.message}", throwable = e)
            throw e
        }
    }

    override fun observeTradesStream(symbol: String): Flow<List<Trade>> {
        logger.i(message = "Observing trade stream: $symbol")

        // Launch WebSocket flow in background if not already active
        if (!activeStreams.contains(symbol)) {
            activeStreams.add(symbol)

            wsDataSource.connectTradeStream(symbol)
                .onEach { tradeUpdate ->

                    // Insert into database
                    val entity = tradeUpdate.toEntity()
                    dao.insertTrades(listOf(entity))
                }
                .catch { e ->
                    logger.e(message = "WebSocket trade stream error: ${e.message}", throwable = e)
                    activeStreams.remove(symbol)
                }
                .launchIn(scope)
        }

        // Return DB Flow for UI observation
        return dao.getRecentTrades(symbol, limit = 100)
            .map { entities ->
                entities.map { it.toDomain() }
            }
            .distinctUntilChanged()
    }

    override suspend fun cleanupOldTrades(olderThan: Long) {
        logger.i(message = "Cleaning up trades older than $olderThan")
        dao.deleteOldTrades(olderThan)
    }
}