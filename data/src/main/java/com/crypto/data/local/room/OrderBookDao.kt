package com.crypto.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderBookDao {

    /**
     * Insert or replace orderbook levels
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLevels(levels: List<OrderBookEntity>)

    /**
     * Clear entire orderbook for a symbol
     */
    @Query("DELETE FROM order_books WHERE symbol = :symbol")
    suspend fun clearOrderBook(symbol: String)

    /**
     * Get orderbook for a symbol
     * Returns Flow for reactive updates
     */
    @Query("""
        SELECT * FROM order_books 
        WHERE symbol = :symbol 
        ORDER BY 
            CASE side WHEN 'ASK' THEN 0 ELSE 1 END,
            CASE side WHEN 'ASK' THEN priceLevel ELSE -priceLevel END
        LIMIT :depth
    """)
    fun getOrderBook(symbol: String, depth: Int = 40): Flow<List<OrderBookEntity>>

    /**
     * Get only bids (buy orders) sorted by price descending
     */
    @Query("""
        SELECT * FROM order_books 
        WHERE symbol = :symbol AND side = 'BID'
        ORDER BY priceLevel DESC
        LIMIT :limit
    """)
    fun getBids(symbol: String, limit: Int = 20): Flow<List<OrderBookEntity>>

    /**
     * Get only asks (sell orders) sorted by price ascending
     */
    @Query("""
        SELECT * FROM order_books 
        WHERE symbol = :symbol AND side = 'ASK'
        ORDER BY priceLevel ASC
        LIMIT :limit
    """)
    fun getAsks(symbol: String, limit: Int = 20): Flow<List<OrderBookEntity>>

    /**
     * Delete specific price level (for diff updates when quantity = 0)
     */
    @Query("DELETE FROM order_books WHERE symbol = :symbol AND priceLevel = :price AND side = :side")
    suspend fun deletePriceLevel(symbol: String, price: Double, side: String)

    /**
     * Update orderbook atomically
     */
    @Transaction
    suspend fun updateOrderBook(symbol: String, levels: List<OrderBookEntity>) {
        clearOrderBook(symbol)
        insertLevels(levels)
    }

    /**
     * Apply diff update (insert/update/delete based on quantity)
     */
    @Transaction
    suspend fun applyDiffUpdate(symbol: String, updates: List<OrderBookEntity>) {
        updates.forEach { level ->
            if (level.quantity == 0.0) {
                // Remove price level
                deletePriceLevel(symbol, level.priceLevel, level.side)
            } else {
                // Insert or update
                insertLevels(listOf(level))
            }
        }
    }
}

