package com.crypto.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * WebSocket depth update response from Binance
 * Stream: wss://stream.binance.com:9443/ws/btcusdt@depth@100ms
 *
 * Example:
 * {
 *   "e": "depthUpdate",
 *   "E": 1234567890,
 *   "s": "BTCUSDT",
 *   "U": 157,
 *   "u": 160,
 *   "b": [["45000.00", "1.234"], ["44999.00", "0"]],
 *   "a": [["45001.00", "0.987"]]
 * }
 */
data class DepthUpdateResponse(
    @SerializedName("e")
    val eventType: String,  // "depthUpdate"

    @SerializedName("E")
    val eventTime: Long,

    @SerializedName("s")
    val symbol: String,  // "BTCUSDT"

    @SerializedName("U")
    val firstUpdateId: Long,

    @SerializedName("u")
    val finalUpdateId: Long,

    @SerializedName("b")
    val bids: List<List<String>>,  // [price, quantity] - quantity=0 means remove

    @SerializedName("a")
    val asks: List<List<String>>   // [price, quantity]
)

