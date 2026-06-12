package com.crypto.data.local.room

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Trade entity for storing recent trades
 */
@Entity(
    tableName = "trades",
    indices = [
        Index(value = ["symbol", "time"]),
        Index(value = ["symbol", "isBuyerMaker"])
    ]
)
data class TradeEntity(
    @PrimaryKey
    val id: Long,                // Trade ID from Binance
    val symbol: String,          // "BTCUSDT"
    val price: Double,           // 45234.50
    val quantity: Double,        // 0.123
    val time: Long,              // Trade timestamp
    val isBuyerMaker: Boolean,   // true = sell, false = buy
    val timestamp: Long = System.currentTimeMillis()
)

