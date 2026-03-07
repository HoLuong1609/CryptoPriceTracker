package com.crypto.data.di

import com.crypto.data.repository.KlineRepositoryImpl
import com.crypto.domain.repository.KlineRepository
import com.crypto.domain.usecase.GetKlinesUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {

    @Binds
    fun bindKlineRepository(
        impl: KlineRepositoryImpl
    ): KlineRepository

    companion object {
        @Provides
        fun provideGetKlinesUseCase(
            repository: KlineRepository
        ): GetKlinesUseCase {
            return GetKlinesUseCase(repository)
        }
    }
}

