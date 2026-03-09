package com.crypto.data.mapper

import com.crypto.data.local.room.MarketCoinEntity
import com.crypto.data.remote.dto.TickerResponse

fun TickerResponse.toEntity(): MarketCoinEntity {

    return MarketCoinEntity(
        symbol = symbol,
        price = lastPrice.toDoubleOrNull() ?: 0.0,
        priceChangePercent = priceChangePercent.toDoubleOrNull() ?: 0.0
    )
}