package com.crypto.domain.model

data class TickerUpdate(
    val symbol: String,
    val price: Double,
    val change: Double
)