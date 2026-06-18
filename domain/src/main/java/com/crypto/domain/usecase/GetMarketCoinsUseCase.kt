package com.crypto.domain.usecase

import com.crypto.domain.model.MarketCoin
import com.crypto.domain.repository.MarketRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Use case to fetch market coins from the repository.
 *
 * @property repository The market repository that provides access to market data
 *
 * @see MarketRepository
 * @see MarketCoin
 */
class GetMarketCoinsUseCase(
    private val repository: MarketRepository
) {

    /**
     * Invoke the use case to fetch market coins.
     *
     * @return Flow emitting a list of [MarketCoin] objects
     */
    operator fun invoke(): Flow<List<MarketCoin>> = flow {
        val coins = repository.getMarketCoins()

        emit(coins)
    }
}