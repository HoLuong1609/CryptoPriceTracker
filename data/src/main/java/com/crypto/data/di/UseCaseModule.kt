package com.crypto.data.di

import com.crypto.domain.repository.KlineRepository
import com.crypto.domain.repository.MarketRepository
import com.crypto.domain.usecase.GetKlinesUseCase
import com.crypto.domain.usecase.GetMarketCoinsUseCase
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
}
