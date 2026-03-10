package com.crypto.data.mapper

import com.crypto.data.local.room.MarketCoinEntity
import com.crypto.domain.model.MarketCoin

fun MarketCoinEntity.toMarketCoin(): MarketCoin {

    return MarketCoin(
        symbol = symbol,
        price = price,
        priceChangePercent = priceChangePercent,
        high = high,
        low = low,
        volume = volume
    )
}