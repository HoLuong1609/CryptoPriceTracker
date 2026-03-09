package com.crypto.data.di

import android.content.Context
import androidx.room.Room
import com.crypto.data.local.room.CryptoDatabase
import com.crypto.data.local.room.MarketDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): CryptoDatabase {

        return Room.databaseBuilder(
            context,
            CryptoDatabase::class.java,
            "crypto_database"
        ).build()

    }

    @Provides
    fun provideMarketDao(
        database: CryptoDatabase
    ): MarketDao {

        return database.marketDao()

    }

}