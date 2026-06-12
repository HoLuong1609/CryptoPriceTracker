package com.crypto.domain.model

/**
 * Domain model representing an orderbook (bid/ask depth)
 */
data class OrderBook(
    val symbol: String,              // "BTCUSDT"
    val bids: List<PriceLevel>,      // Buy orders (descending by price)
    val asks: List<PriceLevel>,      // Sell orders (ascending by price)
    val lastUpdateId: Long,          // Last update ID from Binance
    val timestamp: Long = System.currentTimeMillis()
) {
    /**
     * Calculate spread (difference between best ask and best bid)
     */
    val spread: Double
        get() = if (asks.isNotEmpty() && bids.isNotEmpty()) {
            asks.first().price - bids.first().price
        } else 0.0
    
    /**
     * Calculate spread percentage
     */
    val spreadPercentage: Double
        get() = if (bids.isNotEmpty() && spread != 0.0) {
            (spread / bids.first().price) * 100
        } else 0.0
    
    /**
     * Best bid
     */
    val bestBid: PriceLevel?
        get() = bids.firstOrNull()
    
    /**
     * Best ask
     */
    val bestAsk: PriceLevel?
        get() = asks.firstOrNull()
}

/**
 * Price level in orderbook
 */
data class PriceLevel(
    val price: Double,      // Price level
    val quantity: Double    // Quantity at this price
) {
    /**
     * Total value at this price level
     */
    val total: Double
        get() = price * quantity
}

