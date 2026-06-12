package com.crypto.domain.usecase

import com.crypto.domain.model.Trade
import com.crypto.domain.repository.TradeRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case to observe realtime trade stream
 */
class ObserveTradesUseCase(
    private val repository: TradeRepository
) {
    operator fun invoke(symbol: String): Flow<List<Trade>> {
        return repository.observeTradesStream(symbol)
    }
}

