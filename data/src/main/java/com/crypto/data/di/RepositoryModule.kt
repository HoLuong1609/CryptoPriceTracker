package com.crypto.data.di

import com.crypto.data.repository.KlineRepositoryImpl
import com.crypto.data.repository.MarketRepositoryImpl
import com.crypto.domain.repository.KlineRepository
import com.crypto.domain.repository.MarketRepository
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
}