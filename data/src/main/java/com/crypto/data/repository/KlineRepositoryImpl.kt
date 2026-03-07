package com.crypto.data.repository

import com.crypto.core.result.AppResult
import com.crypto.data.mapper.toDomain
import com.crypto.data.remote.datasource.KlineRemoteDataSource
import com.crypto.domain.model.Kline
import com.crypto.domain.repository.KlineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class KlineRepositoryImpl @Inject constructor(
    private val remote: KlineRemoteDataSource
) : KlineRepository {

    override suspend fun getKlines(
        symbol: String,
        interval: String,
        startTime: Long,
        endTime: Long
    ): Flow<AppResult<List<Kline>>> {

        return remote.fetchKlines(
            symbol,
            interval,
            startTime,
            endTime
        ).map { result ->

            when (result) {
                is AppResult.Success -> {
                    AppResult.Success(
                        result.data.map { it.toDomain() }
                    )
                }
                is AppResult.Error -> result
                is AppResult.Loading -> result
            }
        }
    }
}