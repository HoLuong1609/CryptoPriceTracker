package com.crypto.data.remote.dto

class KlineResponse(
    val symbol: String,
    val interval: String,
    val openingTime: Long,
    val openingPrice: String,
    val highestPrice: String,
    val lowestPrice: String,
    val closingPrice: String,
    val volume: String,
    val closingTime: Long
)