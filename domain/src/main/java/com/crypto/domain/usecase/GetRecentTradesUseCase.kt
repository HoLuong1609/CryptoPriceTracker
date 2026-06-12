package com.crypto.domain.usecase

import com.crypto.domain.model.Trade
import com.crypto.domain.repository.TradeRepository

/**
 * Use case to get recent trades
 */
class GetRecentTradesUseCase(
    private val repository: TradeRepository
) {
    suspend operator fun invoke(symbol: String, limit: Int = 100): List<Trade> {
        return repository.getRecentTrades(symbol, limit)
    }
}

