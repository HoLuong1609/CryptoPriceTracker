package com.crypto.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MarketCoinEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CryptoDatabase : RoomDatabase() {

    abstract fun marketDao(): MarketDao

}