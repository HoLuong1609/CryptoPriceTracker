package com.crypto.data.remote.dto

data class TickerResponse(

    val symbol: String,

    val lastPrice: String,

    val priceChangePercent: String,

    val highPrice: String,

    val lowPrice: String,

    val volume: String
)