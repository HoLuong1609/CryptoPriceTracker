package com.crypto.data.network

/**
 * Network configuration with retry and timeout settings
 */
data class NetworkConfig(
    val baseUrl: String,
    val wsUrl: String,
    val limitMin: Int,
    val limitMax: Int,
    val retryConfig: RetryConfig = RetryConfig(),
    val timeoutConfig: TimeoutConfig = TimeoutConfig()
)

/**
 * Retry configuration for WebSocket connections
 */
data class RetryConfig(
    val maxAttempts: Int = 5,
    val initialDelayMs: Long = 1000,      // 1 second
    val maxDelayMs: Long = 32000,         // 32 seconds
    val backoffMultiplier: Float = 2f     // Exponential: 1s, 2s, 4s, 8s, 16s, 32s
)

/**
 * Timeout configuration for network requests
 */
data class TimeoutConfig(
    val connectTimeoutMs: Long = 10000,   // 10 seconds
    val readTimeoutMs: Long = 20000,      // 20 seconds (0 for WebSocket)
    val writeTimeoutMs: Long = 20000      // 20 seconds
)