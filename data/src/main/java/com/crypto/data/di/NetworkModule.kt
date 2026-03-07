package com.crypto.data.di

import com.crypto.data.BuildConfig
import com.crypto.data.network.NetworkConfig
import com.crypto.data.remote.api.BinanceApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideNetworkConfig(): NetworkConfig {
        val isDebug = BuildConfig.DEBUG

        return NetworkConfig(
            baseUrl = if (isDebug)
                "https://testnet.binance.vision"
            else
                "https://fapi.binance.com",

            wsUrl = if (isDebug)
                "wss://testnet.binance.vision/ws-api/v3"
            else
                "wss://stream.binance.com:9443",

            limitMin = 500,
            limitMax = 1500
        )
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .setStrictness(Strictness.LENIENT)
            .create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        config: NetworkConfig,
        client: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(config.baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideBinanceApi(
        retrofit: Retrofit
    ): BinanceApi {
        return retrofit.create(BinanceApi::class.java)
    }
}