package com.crypto.data.repository

import com.crypto.core.logging.Logger
import com.crypto.core.util.CurrencyFormatter.formatPrice
import com.crypto.data.remote.api.BinanceApi
import com.crypto.data.remote.datasource.OrderBookWebSocketDataSource
import com.crypto.data.remote.dto.PartialDepthResponse
import com.crypto.data.remote.dto.OrderBookResponse
import com.crypto.domain.model.OrderBook
import com.crypto.domain.model.PriceLevel
import com.crypto.domain.repository.OrderBookRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.sample
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of OrderBookRepository
 *
 * Strategy:
 * Load snapshot from REST API → Store in memory (StateFlow)
 * Connect WebSocket for partial depth snapshots → Update memory directly
 * Emit from memory → UI
 */
@Singleton
class OrderBookRepositoryImpl @Inject constructor(
    private val api: BinanceApi,
    private val wsDataSource: OrderBookWebSocketDataSource,
    private val logger: Logger
) : OrderBookRepository {

    // In-memory orderbook cache: symbol -> OrderBook
    private val orderBookCache = mutableMapOf<String, MutableStateFlow<OrderBook?>>()

    override suspend fun getOrderBook(symbol: String, limit: Int): OrderBook {
        logger.i(message = "Fetching orderbook snapshot: $symbol")

        return try {
            val response = api.getOrderBook(symbol, limit)

            // Store in memory cache
            val orderBook = response.toDomainModel(symbol)
            val flow = orderBookCache.getOrPut(symbol) {
                MutableStateFlow(null)
            }
            flow.value = orderBook

            logger.d(message = "Orderbook snapshot cached in memory: ${response.bids.size} bids, ${response.asks.size} asks")

            orderBook
        } catch (e: Exception) {
            logger.e(message = "Failed to fetch orderbook: ${e.message}", throwable = e)
            throw e
        }
    }

    @OptIn(FlowPreview::class)
    override fun observeOrderBook(symbol: String): Flow<OrderBook> {
        logger.i(message = "Observing orderbook: $symbol")

        // Get or create cache for this symbol
        val cache = orderBookCache.getOrPut(symbol) {
            MutableStateFlow(null)
        }

        return callbackFlow {
            // Start WebSocket in this Flow's scope
            val wsJob = wsDataSource.connectDepthStream(symbol)
                .onEach { snapshot ->
                    val bidCount = snapshot.bids?.size ?: 0
                    val askCount = snapshot.asks?.size ?: 0
                    logger.d(message = "Partial depth snapshot: $bidCount bids, $askCount asks")

                    // Update memory cache directly (no DB)
                    val orderBook = snapshot.toDomainModel(symbol)
                    cache.value = orderBook
                }
                .catch { e ->
                    logger.e(message = "WebSocket depth stream error: ${e.message}", throwable = e)
                }
                .launchIn(this)  // Launch in callbackFlow scope

            // Observe memory cache and emit to UI
            cache.sample(300)  // Throttle: emit at most once per 300ms
                .distinctUntilChanged { old, new ->
                    old?.lastUpdateId == new?.lastUpdateId
                }
                .collect { orderBook ->
                if (orderBook != null) {
                    logger.d(message = "Emitting orderbook: ${orderBook.bids.size} bids, ${orderBook.asks.size} asks, spread=${formatPrice(orderBook.spread)}")
                    send(orderBook)
                }
            }

            // Cleanup when Flow canceled
            awaitClose {
                logger.i(message = "Cleaning up orderbook stream: $symbol")
                wsJob.cancel()
                cache.value = null  // Clear cache
            }
        }
    }
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
 * Extension: Convert WebSocket snapshot to domain model
 */
private fun PartialDepthResponse.toDomainModel(symbol: String): OrderBook {
    val bids = this.bids?.map {
        PriceLevel(it[0].toDouble(), it[1].toDouble())
    } ?: emptyList()

    val asks = this.asks?.map {
        PriceLevel(it[0].toDouble(), it[1].toDouble())
    } ?: emptyList()

    return OrderBook(
        symbol = symbol,
        bids = bids.sortedByDescending { it.price },  // Ensure correct sort
        asks = asks.sortedBy { it.price },            // Ensure correct sort
        lastUpdateId = lastUpdateId
    )
}
