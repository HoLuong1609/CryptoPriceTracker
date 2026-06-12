package com.crypto.domain.model

/**
 * Domain model representing a trade
 */
data class Trade(
    val id: Long,               // Trade ID
    val symbol: String,         // "BTCUSDT"
    val price: Double,          // Trade price
    val quantity: Double,       // Trade quantity
    val time: Long,             // Trade timestamp (millis)
    val isBuy: Boolean          // true = buy (taker is buyer), false = sell (taker is seller)
) {
    /**
     * Total value of the trade
     */
    val total: Double
        get() = price * quantity

    /**
     * Trade side as string
     */
    val side: String
        get() = if (isBuy) "BUY" else "SELL"
}

