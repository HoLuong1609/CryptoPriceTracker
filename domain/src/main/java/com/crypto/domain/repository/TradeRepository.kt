package com.crypto.domain.repository

import com.crypto.domain.model.Trade
import kotlinx.coroutines.flow.Flow

/**
 * Repository for trade data
 */
interface TradeRepository {

    /**
     * Get recent trades from REST API
     *
     * @param symbol Trading pair (e.g., "BTCUSDT")
     * @param limit Number of trades to fetch (default 100)
     * @return List of recent trades
     */
    suspend fun getRecentTrades(symbol: String, limit: Int = 100): List<Trade>

    /**
     * Observe realtime trade stream
     *
     * @param symbol Trading pair (e.g., "BTCUSDT")
     * @return Flow of trades
     */
    fun observeTradesStream(symbol: String): Flow<List<Trade>>

    /**
     * Clean up old trades
     * Deletes trades older than specified timestamp
     *
     * @param olderThan Timestamp threshold
     */
    suspend fun cleanupOldTrades(olderThan: Long)
}

