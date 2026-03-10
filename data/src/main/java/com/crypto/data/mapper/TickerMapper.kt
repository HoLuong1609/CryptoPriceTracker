package com.crypto.data.mapper

import com.crypto.data.local.room.MarketCoinEntity
import com.crypto.data.remote.dto.TickerResponse
import com.crypto.domain.model.MarketCoin
import com.crypto.domain.model.Ticker

fun TickerResponse.toMarketCoin(): MarketCoin {

    return MarketCoin(
        symbol = symbol,
        price = lastPrice.toDouble(),
        priceChangePercent = priceChangePercent.toDouble(),
        high = highPrice.toDouble(),
        low = lowPrice.toDouble(),
        volume = volume.toDouble()
    )
}

fun TickerResponse.toEntity(): MarketCoinEntity {

    return MarketCoinEntity(
        symbol = symbol,
        price = lastPrice.toDoubleOrNull() ?: 0.0,
        priceChangePercent = priceChangePercent.toDoubleOrNull() ?: 0.0
    )
}

fun TickerResponse.toDomain(): Ticker {

    return Ticker(
        symbol = symbol,
        lastPrice = lastPrice,
        priceChangePercent = priceChangePercent
    )

}