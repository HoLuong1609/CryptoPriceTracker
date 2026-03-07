package com.crypto.data.remote.datasource

import com.crypto.core.dispatcher.DispatcherProvider
import com.crypto.core.result.AppResult
import com.crypto.data.mapper.toKlineResponse
import com.crypto.data.network.NetworkConfig
import com.crypto.data.remote.api.BinanceApi
import com.crypto.data.remote.dto.KlineResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class KlineRemoteDataSource @Inject constructor(
    private val api: BinanceApi,
    private val config: NetworkConfig,
    private val dispatcher: DispatcherProvider
) {

    fun fetchKlines(
        symbol: String,
        interval: String,
        startTime: Long,
        endTime: Long
    ): Flow<AppResult<List<KlineResponse>>> = flow {

        emit(AppResult.Loading)
        val call = api.getKlines(
            symbol = symbol,
            interval = interval,
            startTime = startTime,
            endTime = endTime,
            limit = config.limitMax
        )

        val response = call.execute()
        if (response.isSuccessful) {

            val mapped = response.body()?.map {
                it.toKlineResponse(symbol, interval)
            }
            mapped?.let {
                emit(AppResult.Success(it) as AppResult<List<KlineResponse>>)
            }
        } else {
            emit(
                AppResult.Error(
                    message = response.message()
                ) as AppResult<List<KlineResponse>>
            )
        }

    }.catch { e ->

        emit(
            AppResult.Error(
                message = e.message ?: "Unknown error",
                throwable = e
            ) as AppResult<List<KlineResponse>>
        )

    }.flowOn(dispatcher.io)
}