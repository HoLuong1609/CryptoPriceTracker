package com.crypto.data.local.room

import androidx.room.Entity
import androidx.room.Index

/**
 * Orderbook price level entity
 *
 * Composite primary key: symbol + priceLevel + side
 * This ensures unique price levels per side
 */
@Entity(
    tableName = "order_books",
    primaryKeys = ["symbol", "priceLevel", "side"],
    indices = [
        Index(value = ["symbol", "side"]),
        Index(value = ["updateId"])
    ]
)
data class OrderBookEntity(
    val symbol: String,          // "BTCUSDT"
    val priceLevel: Double,      // 45234.50
    val quantity: Double,        // 1.234
    val side: String,            // "BID" or "ASK"
    val updateId: Long,          // Last update ID from WebSocket
    val timestamp: Long = System.currentTimeMillis()
)

