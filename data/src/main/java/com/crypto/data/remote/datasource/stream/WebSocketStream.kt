package com.crypto.data.remote.datasource.stream

/**
 * Sealed class representing different WebSocket stream types
 * Each stream has a specific URL format for Binance WebSocket API
 */
sealed class WebSocketStream(open val url: String) {

    /**
     * Global ticker updates - all 24h data for all symbols
     * Stream: !miniTicker@arr
     */
    object Ticker24h : WebSocketStream("!miniTicker@arr")

    /**
     * Partial Book Depth Stream
     * Stream: <symbol>@depth<levels>@<update_speed>
     *
     * @param symbol Trading pair
     * @param level Depth level
     * @param updateSpeed Update frequency
     */
    data class DepthStream(
        val symbol: String,
        val level: Int = 20,
        val updateSpeed: String = "100ms"
    ) : WebSocketStream("${symbol.lowercase()}@depth$level@$updateSpeed") {
        init {
            require(level in listOf(5, 10, 20)) {
                "Level must be 5, 10, or 20. Got: $level"
            }
            require(updateSpeed in listOf("100ms", "1000ms")) {
                "Update speed must be '100ms' or '1000ms'. Got: $updateSpeed"
            }
        }
    }

    /**
     * Trade Stream - individual trades
     * Stream: <symbol>@trade
     *
     * @param symbol Trading pair
     */
    data class TradeStream(
        val symbol: String
    ) : WebSocketStream("${symbol.lowercase()}@trade")

    /**
     * Kline/Candlestick Stream
     * Stream: <symbol>@kline_<interval>
     *
     * @param symbol Trading pair
     * @param interval Time interval
     */
    data class KlineStream(
        val symbol: String,
        val interval: String
    ) : WebSocketStream("${symbol.lowercase()}@kline_$interval") {
        init {
            val validIntervals = listOf(
                "1m", "3m", "5m", "15m", "30m",
                "1h", "2h", "4h", "6h", "8h", "12h",
                "1d", "3d", "1w", "1M"
            )
            require(interval in validIntervals) {
                "Invalid interval: $interval. Valid intervals: $validIntervals"
            }
        }
    }

    /**
     * Book Ticker Stream - best bid/ask for a symbol
     * Stream: <symbol>@bookTicker
     *
     * @param symbol Trading pair
     */
    data class BookTickerStream(
        val symbol: String
    ) : WebSocketStream("${symbol.lowercase()}@bookTicker")
}

/**
 * Extension function to convert WebSocketStream to full WebSocket URL
 *
 * @param baseWsUrl Base WebSocket URL
 * @return Full WebSocket URL
 */
fun WebSocketStream.toWebSocketUrl(baseWsUrl: String): String {
    return "$baseWsUrl/ws/${this.url}"
}

