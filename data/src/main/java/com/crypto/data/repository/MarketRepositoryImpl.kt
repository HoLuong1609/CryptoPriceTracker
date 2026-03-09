package com.crypto.data.repository

import com.crypto.data.local.room.MarketDao
import com.crypto.data.mapper.toEntity
import com.crypto.data.mapper.toMarketCoin
import com.crypto.data.remote.api.BinanceApi
import com.crypto.domain.model.MarketCoin
import com.crypto.domain.repository.MarketRepository
import javax.inject.Inject

class MarketRepositoryImpl @Inject constructor(
    private val api: BinanceApi,
    private val marketDao: MarketDao
) : MarketRepository {

    override suspend fun getMarketCoins(): List<MarketCoin> {

        val tickers = api.get24hrTickers()

        val filtered = tickers
            .filter { it.symbol.endsWith("USDT") }
            .sortedByDescending { it.volume.toDouble() }
            .take(300)

        val entities = filtered.map { it.toEntity() }

        marketDao.insertCoins(entities)

        return filtered.map { it.toMarketCoin() }
    }
}