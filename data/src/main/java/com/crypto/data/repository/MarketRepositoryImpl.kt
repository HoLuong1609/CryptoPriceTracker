package com.crypto.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.crypto.data.local.room.MarketDao
import com.crypto.data.local.room.TickerUpdate
import com.crypto.data.mapper.toEntity
import com.crypto.data.mapper.toMarketCoin
import com.crypto.data.remote.api.BinanceApi
import com.crypto.data.remote.datasource.TickerWebSocketDataSource
import com.crypto.domain.model.MarketCoin
import com.crypto.domain.repository.MarketRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class MarketRepositoryImpl @Inject constructor(
    private val api: BinanceApi,
    private val marketDao: MarketDao,
    private val tickerWebSocketDataSource: TickerWebSocketDataSource
) : MarketRepository {

    private val lastPrices = mutableMapOf<String, Double>()

    override suspend fun getMarketCoins(): List<MarketCoin> {

        val tickers = api.get24hrTickers()

        val filtered = tickers
            .filter { it.symbol?.endsWith("USDT") == true }
            .sortedByDescending { it.volume?.toDoubleOrNull() ?: 0.0 }
            .take(300)

        val entities = filtered.map { it.toEntity() }

        marketDao.insertCoins(entities)

        return filtered.map { it.toMarketCoin() }
    }

    @OptIn(FlowPreview::class)
    override fun startTickerUpdates(): Flow<Unit> {

        return tickerWebSocketDataSource.connect()
            .map { tickers ->
                tickers.mapNotNull { ticker ->
                    val symbol = ticker.symbol
                    val price = ticker.lastPrice?.toDoubleOrNull()
                    val change = ticker.priceChangePercent?.toDoubleOrNull()

                    if (symbol != null && price != null && change != null) {
                        val lastPrice = lastPrices[symbol]

                        if (lastPrice == null || lastPrice != price) {
                            lastPrices[symbol] = price
                            TickerUpdate(symbol, price, change)
                        } else {
                            null
                        }
                    } else null
                }
            }
            .filter { it.isNotEmpty() }

            .debounce(200)

            .onEach { updates ->
                marketDao.updateTickers(updates)
            }
            .map { }
    }

    override fun getPagedMarketCoins(): Flow<PagingData<MarketCoin>> =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                marketDao.getPagedCoins()
            }
        ).flow
            .map { pagingData ->
                pagingData.map { it.toMarketCoin() }
            }
}