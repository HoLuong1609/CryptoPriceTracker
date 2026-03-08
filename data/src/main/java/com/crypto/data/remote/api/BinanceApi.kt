package com.crypto.data.remote.api

import com.crypto.data.remote.dto.TickerResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BinanceApi {

    @GET("/api/v3/klines")
    fun getKlines(
        @Query("symbol") symbol: String,
        @Query("interval") interval: String,
        @Query("startTime") startTime: Long,
        @Query("endTime") endTime: Long,
        @Query("limit") limit: Int,
    ): Call<List<List<Any>>>

    @GET("api/v3/ticker/24hr")
    suspend fun get24hrTickers(): List<TickerResponse>
}