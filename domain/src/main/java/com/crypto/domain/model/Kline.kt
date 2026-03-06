package com.crypto.domain.model

data class Kline(
    val symbol: String,
    val interval: String,
    val openTime: Long,
    val openPrice: Double,
    val highPrice: Double,
    val lowPrice: Double,
    val closePrice: Double,
    val volume: Double,
    val closeTime: Long
)