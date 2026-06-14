package com.crypto.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        MarketCoinEntity::class,
        TradeEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class CryptoDatabase : RoomDatabase() {

    abstract fun marketDao(): MarketDao
    abstract fun tradeDao(): TradeDao

}