package com.crypto.data.network

data class NetworkConfig(
    val baseUrl: String,
    val wsUrl: String,
    val limitMin: Int,
    val limitMax: Int
)