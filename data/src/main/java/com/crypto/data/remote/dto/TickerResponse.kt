package com.crypto.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TickerResponse(

    @SerializedName(value = "s", alternate = ["symbol"])
    val symbol: String?,

    @SerializedName(value = "c", alternate = ["lastPrice"])
    val lastPrice: String?,

    @SerializedName(value = "P", alternate = ["priceChangePercent"])
    val priceChangePercent: String?,

    @SerializedName(value = "h", alternate = ["highPrice"])
    val highPrice: String?,

    @SerializedName(value = "l", alternate = ["lowPrice"])
    val lowPrice: String?,

    @SerializedName(value = "v", alternate = ["volume"])
    val volume: String?
)