package com.crypto.data.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "market_coins")
data class MarketCoinEntity(

    @PrimaryKey
    val symbol: String,

    val price: Double,

    val priceChangePercent: Double,
    
    val high: Double = 0.0,
    
    val low: Double = 0.0,
    
    val volume: Double = 0.0

)