package com.crypto.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Response from Binance GET /api/v3/depth
 *
 * Example:
 * {
 *   "lastUpdateId": 1234567890,
 *   "bids": [["45000.00", "1.234"], ["44999.50", "0.567"]],
 *   "asks": [["45001.00", "0.987"], ["45001.50", "2.345"]]
 * }
 */
data class OrderBookResponse(
    @SerializedName("lastUpdateId")
    val lastUpdateId: Long,

    @SerializedName("bids")
    val bids: List<List<String>>,  // [price, quantity]

    @SerializedName("asks")
    val asks: List<List<String>>   // [price, quantity]
)

