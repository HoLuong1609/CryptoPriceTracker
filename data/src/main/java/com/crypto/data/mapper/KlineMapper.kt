package com.crypto.data.mapper

import com.crypto.data.remote.dto.KlineResponse
import com.crypto.domain.model.Kline

fun List<Any>.toKlineResponse(
    symbol: String,
    interval: String
): KlineResponse {

    return KlineResponse(
        symbol = symbol,
        interval = interval,
        openingTime = (this[0] as? Double)?.toLong() ?: 0L,
        openingPrice = this[1] as? String ?: "0.0",
        highestPrice = this[2] as? String ?: "0.0",
        lowestPrice = this[3] as? String ?: "0.0",
        closingPrice = this[4] as? String ?: "0.0",
        volume = this[5] as? String ?: "0.0",
        closingTime = (this[6] as? Double)?.toLong() ?: 0L
    )
}

fun KlineResponse.toDomain(): Kline {
    return Kline(
        symbol = symbol,
        interval = interval,
        openTime = openingTime,
        openPrice = openingPrice.toDouble(),
        highPrice = highestPrice.toDouble(),
        lowPrice = lowestPrice.toDouble(),
        closePrice = closingPrice.toDouble(),
        volume = volume.toDouble(),
        closeTime = closingTime
    )
}