package com.crypto.data.repository

import com.crypto.data.mapper.toMarketCoin
import com.crypto.data.remote.api.BinanceApi
import com.crypto.domain.model.MarketCoin
import com.crypto.domain.repository.MarketRepository
import javax.inject.Inject

class MarketRepositoryImpl @Inject constructor(
    private val api: BinanceApi
) : MarketRepository {

    override suspend fun getMarketCoins(): List<MarketCoin> {

        return api.get24hrTickers()
            .filter { it.symbol.endsWith("USDT") }
            .sortedByDescending { it.volume }
            .take(300)
            .map { it.toMarketCoin() }
    }
}