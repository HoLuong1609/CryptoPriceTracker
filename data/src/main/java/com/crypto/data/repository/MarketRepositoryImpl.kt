package com.crypto.data.repository

import com.crypto.data.local.room.MarketDao
import com.crypto.data.mapper.toEntity
import com.crypto.data.mapper.toMarketCoin
import com.crypto.data.mapper.toDomain
import com.crypto.data.remote.api.BinanceApi
import com.crypto.data.remote.datasource.TickerWebSocketDataSource
import com.crypto.domain.model.MarketCoin
import com.crypto.domain.model.Ticker
import com.crypto.domain.repository.MarketRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MarketRepositoryImpl @Inject constructor(
    private val api: BinanceApi,
    private val marketDao: MarketDao,
    private val tickerWebSocketDataSource: TickerWebSocketDataSource
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

    override fun observeTicker(): Flow<List<Ticker>> {

        return tickerWebSocketDataSource.connect().map { tickerList ->
            tickerList.map { it.toDomain() }
        }

    }
}