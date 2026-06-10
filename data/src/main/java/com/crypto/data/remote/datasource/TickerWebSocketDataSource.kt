package com.crypto.data.remote.datasource

import com.crypto.core.logging.Logger
import com.crypto.data.remote.datasource.stream.WebSocketStream
import com.crypto.data.remote.dto.TickerResponse
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import javax.inject.Inject

/**
 * WebSocket data source for 24h ticker updates
 * Refactored to use GenericWebSocketDataSource for connection handling
 */
class TickerWebSocketDataSource @Inject constructor(
    private val wsDataSource: GenericWebSocketDataSource,
    private val logger: Logger
) {

    companion object {
        private const val TAG = "TickerWebSocket"
    }
    /**
     * Connect to Binance 24hr ticker updates stream
     * Returns list of TickerResponse for all symbols, updated in real-time
     *
     * Uses !miniTicker@arr stream for smaller payload (~100KB vs ~1MB)
     * miniTicker provides: symbol, close/lastPrice, open, high, low, volume, quoteVolume
     */
    fun connect(): Flow<List<TickerResponse>> {
        logger.i(TAG, "Starting ticker 24h stream connection")

        val typeToken = object : TypeToken<List<TickerResponse>>() {}
        return wsDataSource.connect<List<TickerResponse>>(
            stream = WebSocketStream.Ticker24h,
            type = typeToken.type
        )
        .buffer(capacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
        .also {
            logger.d(TAG, "Ticker WebSocket Flow created")
        }
    }
}