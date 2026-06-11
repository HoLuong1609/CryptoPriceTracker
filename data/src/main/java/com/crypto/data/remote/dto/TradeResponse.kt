package com.crypto.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Response from Binance GET /api/v3/trades
 *
 * Example:
 * {
 *   "id": 28457,
 *   "price": "45234.50",
 *   "qty": "0.12345",
 *   "quoteQty": "5583.45",
 *   "time": 1499865549590,
 *   "isBuyerMaker": true,
 *   "isBestMatch": true
 * }
 */
data class TradeResponse(
    @SerializedName("id")
    val id: Long,

    @SerializedName("price")
    val price: String,

    @SerializedName("qty")
    val qty: String,

    @SerializedName("quoteQty")
    val quoteQty: String? = null,

    @SerializedName("time")
    val time: Long,

    @SerializedName("isBuyerMaker")
    val isBuyerMaker: Boolean,  // true = sell (buyer made the order, seller took it)

    @SerializedName("isBestMatch")
    val isBestMatch: Boolean? = null
)

/**
 * WebSocket trade stream response
 * Stream: wss://stream.binance.com:9443/ws/btcusdt@trade
 *
 * Example:
 * {
 *   "e": "trade",
 *   "E": 1672515782136,
 *   "s": "BTCUSDT",
 *   "t": 12345,
 *   "p": "45234.50",
 *   "q": "0.123",
 *   "T": 1672515782136,
 *   "m": true
 * }
 */
data class TradeStreamResponse(
    @SerializedName("e")
    val eventType: String,  // "trade"

    @SerializedName("E")
    val eventTime: Long,

    @SerializedName("s")
    val symbol: String,

    @SerializedName("t")
    val tradeId: Long,

    @SerializedName("p")
    val price: String,

    @SerializedName("q")
    val quantity: String,

    @SerializedName("T")
    val tradeTime: Long,

    @SerializedName("m")
    val isBuyerMaker: Boolean
)

