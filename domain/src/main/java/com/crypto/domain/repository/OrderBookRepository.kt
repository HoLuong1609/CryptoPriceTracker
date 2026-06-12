package com.crypto.domain.repository

import com.crypto.domain.model.OrderBook
import kotlinx.coroutines.flow.Flow

/**
 * Repository for orderbook data
 */
interface OrderBookRepository {
    
    /**
     * Get orderbook snapshot from REST API
     * 
     * @param symbol Trading pair (e.g., "BTCUSDT")
     * @param limit Number of price levels (default 20)
     * @return Orderbook snapshot
     */
    suspend fun getOrderBook(symbol: String, limit: Int = 20): OrderBook
    
    /**
     * Observe realtime orderbook updates
     * Combines REST snapshot + WebSocket diff updates
     * 
     * @param symbol Trading pair (e.g., "BTCUSDT")
     * @return Flow of orderbook updates
     */
    fun observeOrderBook(symbol: String): Flow<OrderBook>
}

