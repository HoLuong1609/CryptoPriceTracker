package com.crypto.domain.usecase

import com.crypto.core.result.AppResult
import com.crypto.domain.model.Kline
import com.crypto.domain.repository.KlineRepository
import kotlinx.coroutines.flow.Flow

class GetKlinesUseCase(
    private val repository: KlineRepository
) {

    suspend operator fun invoke(
        symbol: String,
        interval: String,
        startTime: Long,
        endTime: Long
    ): Flow<AppResult<List<Kline>>> {

        return repository.getKlines(
            symbol = symbol,
            interval = interval,
            startTime = startTime,
            endTime = endTime
        )
    }
}