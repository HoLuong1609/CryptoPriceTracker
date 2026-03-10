package com.crypto.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TickerResponse(

    @SerializedName("s")
    val symbol: String,

    @SerializedName("c")
    val lastPrice: String,

    @SerializedName("p")
    val priceChangePercent: String,

    val highPrice: String,

    val lowPrice: String,

    val volume: String
)