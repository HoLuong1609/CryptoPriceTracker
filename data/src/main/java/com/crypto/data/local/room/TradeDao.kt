package com.crypto.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TradeDao {

    /**
     * Insert trades (ignore if already exists)
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrades(trades: List<TradeEntity>)

    /**
     * Get recent trades for a symbol
     */
    @Query("""
        SELECT * FROM trades 
        WHERE symbol = :symbol 
        ORDER BY time DESC
        LIMIT :limit
    """)
    fun getRecentTrades(symbol: String, limit: Int = 100): Flow<List<TradeEntity>>

    /**
     * Get buy trades only
     */
    @Query("""
        SELECT * FROM trades 
        WHERE symbol = :symbol AND isBuyerMaker = 0
        ORDER BY time DESC
        LIMIT :limit
    """)
    fun getBuyTrades(symbol: String, limit: Int = 100): Flow<List<TradeEntity>>

    /**
     * Get sell trades only
     */
    @Query("""
        SELECT * FROM trades 
        WHERE symbol = :symbol AND isBuyerMaker = 1
        ORDER BY time DESC
        LIMIT :limit
    """)
    fun getSellTrades(symbol: String, limit: Int = 100): Flow<List<TradeEntity>>

    /**
     * Delete old trades (cleanup)
     * Remove trades older than specified timestamp
     */
    @Query("DELETE FROM trades WHERE time < :timestamp")
    suspend fun deleteOldTrades(timestamp: Long)

    /**
     * Delete all trades for a symbol
     */
    @Query("DELETE FROM trades WHERE symbol = :symbol")
    suspend fun clearTrades(symbol: String)

    /**
     * Get trade count for a symbol
     */
    @Query("SELECT COUNT(*) FROM trades WHERE symbol = :symbol")
    suspend fun getTradeCount(symbol: String): Int
}

