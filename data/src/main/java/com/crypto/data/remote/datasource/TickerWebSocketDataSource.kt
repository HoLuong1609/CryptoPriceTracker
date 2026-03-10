package com.crypto.data.remote.datasource

import com.crypto.data.network.NetworkConfig
import com.crypto.data.remote.dto.TickerResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.retryWhen
import okhttp3.*
import javax.inject.Inject

class TickerWebSocketDataSource @Inject constructor(
    private val client: OkHttpClient,
    private val config: NetworkConfig,
    private val gson: Gson
) {

    fun connect(): Flow<List<TickerResponse>> = callbackFlow {

        val request = Request.Builder()
            .url("${config.wsUrl}/ws/!ticker@arr")
            .build()

        val listener = object : WebSocketListener() {

            override fun onMessage(webSocket: WebSocket, text: String) {

                val type = object : TypeToken<List<TickerResponse>>() {}.type
                val tickers: List<TickerResponse> = gson.fromJson(text, type)

                trySend(tickers)
            }

            override fun onFailure(
                webSocket: WebSocket,
                t: Throwable,
                response: Response?
            ) {
                close(t) // trigger retry
            }
        }

        val socket = client.newWebSocket(request, listener)

        awaitClose {
            socket.close(1000, null)
        }
    }.retryWhen { cause, attempt ->
        delay(10000)
        true // retry again
    }
}