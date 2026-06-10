package com.crypto.data.remote.datasource

import com.crypto.core.logging.Logger
import com.crypto.data.network.NetworkConfig
import com.crypto.data.network.RetryConfig
import com.crypto.data.remote.datasource.stream.WebSocketStream
import com.crypto.data.remote.datasource.stream.toWebSocketUrl
import com.google.gson.Gson
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.retryWhen
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Named
import kotlin.math.min
import kotlin.math.pow

/**
 * Generic WebSocket data source that can handle any stream type
 * Provides automatic retry with exponential backoff
 */
class GenericWebSocketDataSource @Inject constructor(
    @Named("websocket") private val client: OkHttpClient,
    private val config: NetworkConfig,
    private val gson: Gson,
    private val logger: Logger
) {

    companion object {
        private const val TAG = "GenericWebSocket"
    }

    /**
     * Connect to any WebSocket stream and parse JSON response
     *
     * @param T The type to deserialize JSON into
     * @param stream The WebSocket stream to connect to
     * @param type The Type for deserializing JSON
     * @return Flow emitting parsed objects of type T
     */
    fun <T> connect(
        stream: WebSocketStream,
        type: Type
    ): Flow<T> = callbackFlow {
        val url = stream.toWebSocketUrl(config.wsUrl)
        logger.d(TAG, "Connecting to WebSocket: $url")

        val listener = object : WebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: Response) {
                logger.i(TAG, "WebSocket connected: $stream")
                logger.d(TAG, "Response code: ${response.code}")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    val data: T = gson.fromJson(text, type)
                    val result = trySend(data)
                    if (!result.isSuccess) {
                        logger.w(TAG, "Failed to send WebSocket data (buffer full?)")
                    }
                } catch (e: Exception) {
                    logger.e(TAG, "Failed to parse WebSocket message", e)
                    // Don't close connection, continue listening
                }
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                logger.d(TAG, "Received binary message: ${bytes.size} bytes")
                try {
                    val text = bytes.utf8()
                    val data: T = gson.fromJson(text, type)
                    val result = trySend(data)
                    if (!result.isSuccess) {
                        logger.w(TAG, "Failed to send binary WebSocket data")
                    }
                } catch (e: Exception) {
                    logger.e(TAG, "Failed to parse binary WebSocket message", e)
                }
            }

            override fun onFailure(
                webSocket: WebSocket,
                t: Throwable,
                response: Response?
            ) {
                logger.e(TAG, "WebSocket connection failed: ${t.message}, response code: ${response?.code}", t)
                close(t) // Trigger retry
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                logger.d(TAG, "WebSocket closing: code=$code, reason=$reason")
                webSocket.close(1000, null)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                logger.i(TAG, "WebSocket closed: code=$code, reason=$reason")
            }
        }

        val request = Request.Builder()
            .url(url)
            .build()

        val socket = client.newWebSocket(request, listener)
        logger.d(TAG, "WebSocket instance created")

        awaitClose {
            logger.d(TAG, "Closing WebSocket: $stream")
            socket.close(1000, null)
        }
    }
    .retryWhen { cause, attempt ->
        val delayMs = calculateBackoffDelay(attempt, config.retryConfig)
        logger.w(TAG, "WebSocket retry attempt ${attempt + 1}/${config.retryConfig.maxAttempts} after ${delayMs}ms: ${cause.message}")

        if (attempt < config.retryConfig.maxAttempts) {
            delay(delayMs)
            true // Retry
        } else {
            logger.e(TAG, "WebSocket max retries (${config.retryConfig.maxAttempts}) exceeded", cause)
            false // Stop retrying
        }
    }
    .catch { cause ->
        logger.e(TAG, "WebSocket stream failed after all retries", cause)
        throw cause // Re-throw so repository can handle
    }

    /**
     * Calculate exponential backoff delay
     * Formula: initialDelay * (multiplier ^ attempt), capped at maxDelay
     */
    private fun calculateBackoffDelay(attempt: Long, retryConfig: RetryConfig): Long {
        val delay = retryConfig.initialDelayMs * 
            retryConfig.backoffMultiplier.pow(attempt.toInt())
        return min(delay.toLong(), retryConfig.maxDelayMs)
    }
}

