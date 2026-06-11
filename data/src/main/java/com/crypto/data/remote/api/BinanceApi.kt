package com.crypto.data.remote.api

import com.crypto.data.remote.dto.OrderBookResponse
import com.crypto.data.remote.dto.TickerResponse
import com.crypto.data.remote.dto.TradeResponse
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

    /**
     * Get orderbook depth (snapshot)
     *
     * @param symbol Trading pair (e.g., "BTCUSDT")
     * @param limit Number of price levels (5, 10, 20, 50, 100, 500, 1000, 5000)
     * @return Orderbook snapshot with bids and asks
     */
    @GET("api/v3/depth")
    suspend fun getOrderBook(
        @Query("symbol") symbol: String,
        @Query("limit") limit: Int = 20
    ): OrderBookResponse

    /**
     * Get recent trades
     *
     * @param symbol Trading pair (e.g., "BTCUSDT")
     * @param limit Number of trades to return (max 1000, default 500)
     * @return List of recent trades
     */
    @GET("api/v3/trades")
    suspend fun getRecentTrades(
        @Query("symbol") symbol: String,
        @Query("limit") limit: Int = 100
    ): List<TradeResponse>
}