package com.crypto.data.mapper

import com.crypto.data.local.room.TradeEntity
import com.crypto.data.remote.dto.TradeResponse
import com.crypto.data.remote.dto.TradeStreamResponse
import com.crypto.domain.model.Trade

/**
 * Mapper functions for Trade domain
 * Converts between Data layer models (Entity, DTO) and Domain models
 */

/**
 * Convert REST API TradeResponse to TradeEntity for Room database
 */
fun TradeResponse.toEntity(symbol: String): TradeEntity {
    return TradeEntity(
        id = id,
        symbol = symbol,
        price = price.toDouble(),
        quantity = qty.toDouble(),
        time = time,
        isBuyerMaker = isBuyerMaker
    )
}

/**
 * Convert REST API TradeResponse to Domain Trade model
 */
fun TradeResponse.toDomain(symbol: String): Trade {
    return Trade(
        id = id,
        symbol = symbol,
        price = price.toDouble(),
        quantity = qty.toDouble(),
        time = time,
        isBuy = !isBuyerMaker  // Invert: if buyer is maker, taker is seller
    )
}

/**
 * Convert WebSocket TradeStreamResponse to TradeEntity for Room database
 */
fun TradeStreamResponse.toEntity(): TradeEntity {
    return TradeEntity(
        id = tradeId,
        symbol = symbol,
        price = price.toDouble(),
        quantity = quantity.toDouble(),
        time = tradeTime,
        isBuyerMaker = isBuyerMaker
    )
}

/**
 * Convert Room TradeEntity to Domain Trade model
 */
fun TradeEntity.toDomain(): Trade {
    return Trade(
        id = id,
        symbol = symbol,
        price = price,
        quantity = quantity,
        time = time,
        isBuy = !isBuyerMaker  // Invert: if buyer is maker, taker is seller
    )
}

