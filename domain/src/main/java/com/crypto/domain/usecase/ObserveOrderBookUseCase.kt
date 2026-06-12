package com.crypto.domain.usecase

import com.crypto.domain.model.OrderBook
import com.crypto.domain.repository.OrderBookRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case to observe realtime orderbook updates
 */
class ObserveOrderBookUseCase(
    private val repository: OrderBookRepository
) {
    operator fun invoke(symbol: String): Flow<OrderBook> {
        return repository.observeOrderBook(symbol)
    }
}

