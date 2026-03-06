package com.crypto.domain.repository

import com.crypto.core.result.AppResult
import com.crypto.domain.model.Kline
import kotlinx.coroutines.flow.Flow

interface KlineRepository {

    suspend fun getKlines(
        symbol: String,
        interval: String,
        startTime: Long,
        endTime: Long
    ): Flow<AppResult<List<Kline>>>
}