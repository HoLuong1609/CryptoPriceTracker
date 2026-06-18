package com.crypto.data.di

import com.crypto.domain.network.NetworkMonitor
import com.crypto.domain.repository.KlineRepository
import com.crypto.domain.repository.MarketRepository
import com.crypto.domain.repository.OrderBookRepository
import com.crypto.domain.repository.TradeRepository
import com.crypto.domain.usecase.GetKlinesUseCase
import com.crypto.domain.usecase.GetMarketCoinsUseCase
import com.crypto.domain.usecase.GetOrderBookUseCase
import com.crypto.domain.usecase.GetPagedMarketCoinsUseCase
import com.crypto.domain.usecase.GetRecentTradesUseCase
import com.crypto.domain.usecase.ObserveNetworkStatusUseCase
import com.crypto.domain.usecase.ObserveOrderBookUseCase
import com.crypto.domain.usecase.ObserveTickerUpdatesUseCase
import com.crypto.domain.usecase.ObserveTradesUseCase
import com.crypto.domain.usecase.StartTickerUpdatesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetKlinesUseCase(
        repository: KlineRepository
    ): GetKlinesUseCase {
        return GetKlinesUseCase(repository)
    }

    @Provides
    fun provideGetMarketCoinsUseCase(
        repository: MarketRepository
    ): GetMarketCoinsUseCase {

        return GetMarketCoinsUseCase(repository)
    }

    @Provides
    fun provideStartTickerUpdatesUseCase(
        repository: MarketRepository
    ): StartTickerUpdatesUseCase {

        return StartTickerUpdatesUseCase(repository)

    }

    @Provides
    fun provideGetPagedMarketCoinsUseCase(
        repository: MarketRepository
    ): GetPagedMarketCoinsUseCase {

        return GetPagedMarketCoinsUseCase(repository)

    }

    @Provides
    fun provideObserveTickerUpdatesUseCase(
        repository: MarketRepository
    ): ObserveTickerUpdatesUseCase {

        return ObserveTickerUpdatesUseCase(repository)

    }

    @Provides
    fun provideObserveNetworkStatusUseCase(
        networkMonitor: NetworkMonitor
    ): ObserveNetworkStatusUseCase {

        return ObserveNetworkStatusUseCase(networkMonitor)

    }

    // Orderbook UseCases
    @Provides
    fun provideGetOrderBookUseCase(
        repository: OrderBookRepository
    ): GetOrderBookUseCase {
        return GetOrderBookUseCase(repository)
    }

    @Provides
    fun provideObserveOrderBookUseCase(
        repository: OrderBookRepository
    ): ObserveOrderBookUseCase {
        return ObserveOrderBookUseCase(repository)
    }

    // Trades UseCases
    @Provides
    fun provideGetRecentTradesUseCase(
        repository: TradeRepository
    ): GetRecentTradesUseCase {
        return GetRecentTradesUseCase(repository)
    }

    @Provides
    fun provideObserveTradesUseCase(
        repository: TradeRepository
    ): ObserveTradesUseCase {
        return ObserveTradesUseCase(repository)
    }
}
