package com.crypto.domain.model

data class MarketCoin(

    val symbol: String,

    val price: Double,

    val priceChangePercent: Double,

    val high: Double,

    val low: Double,

    val volume: Double
)