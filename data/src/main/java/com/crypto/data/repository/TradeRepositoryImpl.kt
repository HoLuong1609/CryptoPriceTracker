package com.crypto.data.repository

import com.crypto.core.logging.Logger
import com.crypto.data.local.room.TradeDao
import com.crypto.data.local.room.TradeEntity
import com.crypto.data.remote.api.BinanceApi
import com.crypto.data.remote.datasource.TradeWebSocketDataSource
import com.crypto.data.remote.dto.TradeResponse
import com.crypto.data.remote.dto.TradeStreamResponse
import com.crypto.domain.model.Trade
import com.crypto.domain.repository.TradeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * Implementation of TradeRepository
 *
 * Strategy:
 * Load recent trades from REST API → Store in DB
 * Connect WebSocket for new trades → Insert into DB
 * Observe DB changes → Emit to UI
 */
class TradeRepositoryImpl @Inject constructor(
    private val api: BinanceApi,
    private val wsDataSource: TradeWebSocketDataSource,
    private val dao: TradeDao,
    private val logger: Logger
) : TradeRepository {

    override suspend fun getRecentTrades(symbol: String, limit: Int): List<Trade> {
        logger.i(message = "Fetching recent trades: $symbol")

        return try {
            val response = api.getRecentTrades(symbol, limit)

            // Convert to entities and store
            val entities = response.map { it.toEntity() }
            dao.insertTrades(entities)

            logger.d(message = "${entities.size} trades stored")

            // Return domain models
            response.map { it.toDomainModel() }
        } catch (e: Exception) {
            logger.e(message = "Failed to fetch trades: ${e.message}", throwable = e)
            throw e
        }
    }

    override fun observeTradesStream(symbol: String): Flow<List<Trade>> {
        logger.i(message = "Observing trade stream: $symbol")

        // Flow 1: Connect to WebSocket
        val wsFlow = wsDataSource.connectTradeStream(symbol)
            .onEach { tradeUpdate ->
                logger.d(message = "New trade: ${tradeUpdate.price} @ ${tradeUpdate.quantity}")

                // Insert into database
                val entity = tradeUpdate.toEntity()
                dao.insertTrades(listOf(entity))
            }

        // Flow 2: Observe database
        return dao.getRecentTrades(symbol, limit = 100)
            .map { entities ->
                entities.map { it.toDomainModel() }
            }
            .distinctUntilChanged()
    }

    override suspend fun cleanupOldTrades(olderThan: Long) {
        logger.i(message = "Cleaning up trades older than $olderThan")
        dao.deleteOldTrades(olderThan)
    }
}

/**
 * Extension: Convert TradeResponse to TradeEntity
 */
private fun TradeResponse.toEntity(): TradeEntity {
    return TradeEntity(
        id = id,
        symbol = "", // Will be set by repository
        price = price.toDouble(),
        quantity = qty.toDouble(),
        time = time,
        isBuyerMaker = isBuyerMaker
    )
}

/**
 * Extension: Convert TradeResponse to domain model
 */
private fun TradeResponse.toDomainModel(): Trade {
    return Trade(
        id = id,
        symbol = "", // Will be set by repository
        price = price.toDouble(),
        quantity = qty.toDouble(),
        time = time,
        isBuy = !isBuyerMaker  // Invert: if buyer is maker, taker is seller
    )
}

/**
 * Extension: Convert TradeStreamResponse to TradeEntity
 */
private fun TradeStreamResponse.toEntity(): TradeEntity {
    return TradeEntity(
        id = tradeId,
        symbol = symbol,
        price = price.toDouble(),
        quantity = quantity.toDouble(),
        time = tradeTime,
        isBuyerMaker = isBuyerMaker
    )
}

/**
 * Extension: Convert TradeEntity to domain model
 */
private fun TradeEntity.toDomainModel(): Trade {
    return Trade(
        id = id,
        symbol = symbol,
        price = price,
        quantity = quantity,
        time = time,
        isBuy = !isBuyerMaker  // Invert: if buyer is maker, taker is seller
    )
}

