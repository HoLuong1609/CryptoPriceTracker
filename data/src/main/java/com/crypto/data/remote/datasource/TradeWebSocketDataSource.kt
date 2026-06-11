package com.crypto.data.remote.datasource

import com.crypto.core.logging.Logger
import com.crypto.data.remote.datasource.stream.WebSocketStream
import com.crypto.data.remote.dto.TradeStreamResponse
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * WebSocket data source for Trade stream
 *
 * Connects to: wss://stream.binance.com:9443/ws/btcusdt@trade
 * Pushes individual trade events in real-time
 */
class TradeWebSocketDataSource @Inject constructor(
    private val wsDataSource: GenericWebSocketDataSource,
    private val logger: Logger
) {
    /**
     * Connect to trade stream for a symbol
     *
     * @param symbol Trading pair (e.g., "BTCUSDT")
     * @return Flow of trade updates
     */
    fun connectTradeStream(symbol: String): Flow<TradeStreamResponse> {
        logger.i(message = "Starting trade stream: $symbol")

        val stream = WebSocketStream.TradeStream(symbol)

        return wsDataSource.connect(
            stream = stream,
            type = object : TypeToken<TradeStreamResponse>() {}.type
        )
    }
}

