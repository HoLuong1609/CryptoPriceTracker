package com.crypto.data.remote.datasource

import com.crypto.core.logging.Logger
import com.crypto.data.remote.datasource.stream.WebSocketStream
import com.crypto.data.remote.dto.PartialDepthResponse
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * WebSocket data source for Orderbook depth updates
 *
 * Connects to: wss://stream.binance.com:9443/ws/btcusdt@depth20@100ms
 * Updates every 100ms with top 20 price levels
 */
class OrderBookWebSocketDataSource @Inject constructor(
    private val wsDataSource: GenericWebSocketDataSource,
    private val logger: Logger
) {
    /**
     * Connect to partial depth stream for a symbol
     *
     * @param symbol Trading pair (e.g., "BTCUSDT")
     * @param level Number of price levels (5, 10, 20)
     * @param updateSpeed Update frequency ("100ms" or "1000ms")
     * @return Flow of partial depth snapshots
     */
    fun connectDepthStream(
        symbol: String,
        level: Int = 20,
        updateSpeed: String = "100ms"
    ): Flow<PartialDepthResponse> {
        logger.i(message = "Starting orderbook stream: $symbol (level=$level, speed=$updateSpeed)")

        val stream = WebSocketStream.DepthStream(symbol, level, updateSpeed)

        return wsDataSource.connect(
            stream = stream,
            type = object : TypeToken<PartialDepthResponse>() {}.type
        )
    }
}

