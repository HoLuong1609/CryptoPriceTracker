package com.crypto.data.di

import com.crypto.core.logging.Logger
import com.crypto.data.BuildConfig
import com.crypto.data.logging.DebugLogger
import com.crypto.data.logging.ReleaseLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoggerModule {

    @Provides
    @Singleton
    fun provideLogger(): Logger {
        return if (BuildConfig.DEBUG) {
            DebugLogger()
        } else {
            ReleaseLogger()
        }
    }
}

