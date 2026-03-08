package com.crypto.data.mapper

import com.crypto.data.remote.dto.TickerResponse
import com.crypto.domain.model.MarketCoin

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