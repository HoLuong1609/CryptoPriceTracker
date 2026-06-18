package com.crypto.domain.usecase

import com.crypto.domain.model.TickerUpdate
import com.crypto.domain.repository.MarketRepository
import kotlinx.coroutines.flow.Flow

class ObserveTickerUpdatesUseCase(private val repository: MarketRepository) {

    operator fun invoke(): Flow<Map<String, TickerUpdate>> = repository.observeTickerUpdates()
}
