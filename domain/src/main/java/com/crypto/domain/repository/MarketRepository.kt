package com.crypto.domain.repository

import androidx.paging.PagingData
import com.crypto.domain.model.MarketCoin
import com.crypto.domain.model.TickerUpdate
import kotlinx.coroutines.flow.Flow

interface MarketRepository {

    suspend fun getMarketCoins(): List<MarketCoin>
    fun startTickerUpdates(): Flow<Unit>
    fun getPagedMarketCoins(): Flow<PagingData<MarketCoin>>

    // Observe realtime price updates without database invalidation
    fun observeTickerUpdates(): Flow<Map<String, TickerUpdate>>
}