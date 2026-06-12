package com.crypto.data.repository

import com.crypto.core.logging.Logger
import com.crypto.data.local.room.OrderBookDao
import com.crypto.data.local.room.OrderBookEntity
import com.crypto.data.remote.api.BinanceApi
import com.crypto.data.remote.datasource.OrderBookWebSocketDataSource
import com.crypto.data.remote.dto.DepthUpdateResponse
import com.crypto.data.remote.dto.OrderBookResponse
import com.crypto.domain.model.OrderBook
import com.crypto.domain.model.PriceLevel
import com.crypto.domain.repository.OrderBookRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of OrderBookRepository
 *
 * Strategy:
 * Load snapshot from REST API → Store in DB
 * Connect WebSocket for diff updates → Apply to DB in background
 * Observe DB changes → Emit to UI
 */
@Singleton
class OrderBookRepositoryImpl @Inject constructor(
    private val api: BinanceApi,
    private val wsDataSource: OrderBookWebSocketDataSource,
    private val dao: OrderBookDao,
    private val logger: Logger
) : OrderBookRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val activeStreams = mutableSetOf<String>()

    override suspend fun getOrderBook(symbol: String, limit: Int): OrderBook {
        logger.i(message = "Fetching orderbook snapshot: $symbol")

        return try {
            val response = api.getOrderBook(symbol, limit)

            // Convert to entities and store
            val entities = response.toEntities(symbol)
            dao.updateOrderBook(symbol, entities)

            logger.d(message = "Orderbook snapshot stored: ${entities.size} levels")

            // Return domain model
            response.toDomainModel(symbol)
        } catch (e: Exception) {
            logger.e(message = "Failed to fetch orderbook: ${e.message}", throwable = e)
            throw e
        }
    }

    override fun observeOrderBook(symbol: String): Flow<OrderBook> {
        logger.i(message = "Observing orderbook: $symbol")

        // Start WebSocket in background if not already active
        if (!activeStreams.contains(symbol)) {
            activeStreams.add(symbol)
            wsDataSource.connectDepthStream(symbol)
                .onEach { update ->
                    logger.d(message = "Depth update: ${update.bids.size} bids, ${update.asks.size} asks")
                    applyDiffUpdate(symbol, update)
                }
                .launchIn(scope)
        }

        // Return database flow (updates automatically from WebSocket)
        return dao.getOrderBook(symbol, depth = 40)
            .map { entities ->
                entities.toOrderBook(symbol)
            }
            .distinctUntilChanged()
    }

    /**
     * Apply diff update from WebSocket
     *
     * Binance diff update rules:
     * - If quantity = 0, remove price level
     * - If quantity > 0, insert or update price level
     */
    private suspend fun applyDiffUpdate(symbol: String, update: DepthUpdateResponse) {
        val entities = mutableListOf<OrderBookEntity>()

        // Process bids
        update.bids.forEach { level ->
            val price = level[0].toDouble()
            val quantity = level[1].toDouble()

            entities.add(
                OrderBookEntity(
                    symbol = symbol,
                    priceLevel = price,
                    quantity = quantity,
                    side = "BID",
                    updateId = update.finalUpdateId
                )
            )
        }

        // Process asks
        update.asks.forEach { level ->
            val price = level[0].toDouble()
            val quantity = level[1].toDouble()

            entities.add(
                OrderBookEntity(
                    symbol = symbol,
                    priceLevel = price,
                    quantity = quantity,
                    side = "ASK",
                    updateId = update.finalUpdateId
                )
            )
        }

        // Apply diff (inserts, updates, or deletes)
        dao.applyDiffUpdate(symbol, entities)
    }
}

/**
 * Extension: Convert API response to entities
 */
private fun OrderBookResponse.toEntities(symbol: String): List<OrderBookEntity> {
    val entities = mutableListOf<OrderBookEntity>()

    // Convert bids
    bids.forEach { level ->
        entities.add(
            OrderBookEntity(
                symbol = symbol,
                priceLevel = level[0].toDouble(),
                quantity = level[1].toDouble(),
                side = "BID",
                updateId = lastUpdateId
            )
        )
    }

    // Convert asks
    asks.forEach { level ->
        entities.add(
            OrderBookEntity(
                symbol = symbol,
                priceLevel = level[0].toDouble(),
                quantity = level[1].toDouble(),
                side = "ASK",
                updateId = lastUpdateId
            )
        )
    }

    return entities
}

/**
 * Extension: Convert API response to domain model
 */
private fun OrderBookResponse.toDomainModel(symbol: String): OrderBook {
    return OrderBook(
        symbol = symbol,
        bids = bids.map { PriceLevel(it[0].toDouble(), it[1].toDouble()) },
        asks = asks.map { PriceLevel(it[0].toDouble(), it[1].toDouble()) },
        lastUpdateId = lastUpdateId
    )
}

/**
 * Extension: Convert entities to domain model
 */
private fun List<OrderBookEntity>.toOrderBook(symbol: String): OrderBook {
    val bids = this
        .filter { it.side == "BID" }
        .sortedByDescending { it.priceLevel }
        .map { PriceLevel(it.priceLevel, it.quantity) }

    val asks = this
        .filter { it.side == "ASK" }
        .sortedBy { it.priceLevel }
        .map { PriceLevel(it.priceLevel, it.quantity) }

    return OrderBook(
        symbol = symbol,
        bids = bids,
        asks = asks,
        lastUpdateId = maxOfOrNull { it.updateId } ?: 0L
    )
}

