package com.crypto.domain.repository

import com.crypto.domain.model.MarketCoin

interface MarketRepository {

    suspend fun getMarketCoins(): List<MarketCoin>
}