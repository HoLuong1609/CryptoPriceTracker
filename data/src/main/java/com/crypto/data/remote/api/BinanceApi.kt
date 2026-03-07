package com.crypto.data.remote.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BinanceApi {

    @GET("/fapi/v1/klines")
    fun getKlines(
        @Query("symbol") symbol: String,
        @Query("interval") interval: String,
        @Query("startTime") startTime: Long,
        @Query("endTime") endTime: Long,
        @Query("limit") limit: Int,
    ): Call<List<List<Any>>>
}