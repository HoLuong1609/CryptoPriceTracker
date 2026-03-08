package com.crypto.domain.usecase

import com.crypto.domain.model.MarketCoin
import com.crypto.domain.repository.MarketRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetMarketCoinsUseCase(
    private val repository: MarketRepository
) {

    operator fun invoke(): Flow<List<MarketCoin>> = flow {

        val coins = repository.getMarketCoins()

        emit(coins)
    }
}