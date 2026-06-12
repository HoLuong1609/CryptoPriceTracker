package com.crypto.domain.usecase

import com.crypto.domain.model.OrderBook
import com.crypto.domain.repository.OrderBookRepository

/**
 * Use case to get orderbook snapshot
 */
class GetOrderBookUseCase(
    private val repository: OrderBookRepository
) {
    suspend operator fun invoke(symbol: String, limit: Int = 20): OrderBook {
        return repository.getOrderBook(symbol, limit)
    }
}

