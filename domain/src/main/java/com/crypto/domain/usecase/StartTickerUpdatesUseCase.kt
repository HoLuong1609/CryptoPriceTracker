package com.crypto.domain.usecase

import com.crypto.domain.repository.MarketRepository

class StartTickerUpdatesUseCase(
    private val repository: MarketRepository
) {

    operator fun invoke() =
        repository.startTickerUpdates()

}