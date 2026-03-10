package com.crypto.domain.repository

import com.crypto.domain.model.MarketCoin
import com.crypto.domain.model.Ticker
import kotlinx.coroutines.flow.Flow

interface MarketRepository {

    suspend fun getMarketCoins(): List<MarketCoin>
    fun observeTicker(): Flow<List<Ticker>>
}