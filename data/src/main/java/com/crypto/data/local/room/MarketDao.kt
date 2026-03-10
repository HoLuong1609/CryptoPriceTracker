package com.crypto.data.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface MarketDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoins(coins: List<MarketCoinEntity>)

    @Query("SELECT * FROM market_coins ORDER BY symbol")
    fun getPagedCoins(): PagingSource<Int, MarketCoinEntity>

    @Query(
        """
    UPDATE market_coins 
    SET price = :price,
    priceChangePercent = :change
    WHERE symbol = :symbol
    """
    )
    suspend fun updateTicker(
        symbol: String,
        price: Double,
        change: Double
    )

    @Transaction
    suspend fun updateTickers(updates: List<TickerUpdate>) {
        updates.forEach { update ->
            updateTicker(update.symbol, update.price, update.change)
        }
    }
}

data class TickerUpdate(
    val symbol: String,
    val price: Double,
    val change: Double
)