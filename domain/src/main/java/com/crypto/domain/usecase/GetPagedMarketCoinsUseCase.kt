package com.crypto.domain.usecase

import androidx.paging.PagingData
import com.crypto.domain.model.MarketCoin
import com.crypto.domain.repository.MarketRepository
import kotlinx.coroutines.flow.Flow

class GetPagedMarketCoinsUseCase(
    private val repository: MarketRepository
) {
    operator fun invoke(): Flow<PagingData<MarketCoin>> {
        return repository.getPagedMarketCoins()
    }
}