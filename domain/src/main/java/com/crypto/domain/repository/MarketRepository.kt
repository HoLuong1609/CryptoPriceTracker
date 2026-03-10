package com.crypto.domain.repository

import androidx.paging.PagingData
import com.crypto.domain.model.MarketCoin
import kotlinx.coroutines.flow.Flow

interface MarketRepository {

    suspend fun getMarketCoins(): List<MarketCoin>
    fun startTickerUpdates(): Flow<Unit>
    fun getPagedMarketCoins(): Flow<PagingData<MarketCoin>>
}