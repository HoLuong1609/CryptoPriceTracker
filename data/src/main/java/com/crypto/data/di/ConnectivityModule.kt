package com.crypto.data.di

import com.crypto.data.network.NetworkMonitorImpl
import com.crypto.domain.network.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ConnectivityModule {

    @Binds
    @Singleton
    abstract fun bindNetworkMonitor(
        impl: NetworkMonitorImpl
    ): NetworkMonitor
}
