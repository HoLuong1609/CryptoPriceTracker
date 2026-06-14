package com.crypto.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Partial Book Depth Stream Response
 * Stream: <symbol>@depth<levels>@<speed>
 */
data class PartialDepthResponse(
    @SerializedName("lastUpdateId")
    val lastUpdateId: Long,

    @SerializedName("bids")
    val bids: List<List<String>>? = null,

    @SerializedName("asks")
    val asks: List<List<String>>? = null
)

