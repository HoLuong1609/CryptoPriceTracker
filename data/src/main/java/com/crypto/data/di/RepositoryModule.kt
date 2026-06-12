package com.crypto.data.di

import com.crypto.data.repository.KlineRepositoryImpl
import com.crypto.data.repository.MarketRepositoryImpl
import com.crypto.data.repository.OrderBookRepositoryImpl
import com.crypto.data.repository.TradeRepositoryImpl
import com.crypto.domain.repository.KlineRepository
import com.crypto.domain.repository.MarketRepository
import com.crypto.domain.repository.OrderBookRepository
import com.crypto.domain.repository.TradeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindKlineRepository(
        impl: KlineRepositoryImpl
    ): KlineRepository

    @Binds
    abstract fun bindMarketRepository(
        impl: MarketRepositoryImpl
    ): MarketRepository

    @Binds
    abstract fun bindOrderBookRepository(
        impl: OrderBookRepositoryImpl
    ): OrderBookRepository

    @Binds
    abstract fun bindTradeRepository(
        impl: TradeRepositoryImpl
    ): TradeRepository
}