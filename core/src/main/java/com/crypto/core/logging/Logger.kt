package com.crypto.core.logging

enum class LogLevel {
    DEBUG, INFO, WARNING, ERROR
}

const val LOG_TAG = "CryptoPriceTracker"

/**
 * Logger interface for application-wide logging
 * Provides different implementations for debug and release builds
 */
interface Logger {
    /**
     * Log a message with specified level
     */
    fun log(tag: String = LOG_TAG, message: String, level: LogLevel = LogLevel.INFO)

    /**
     * Log debug message (only in debug builds)
     */
    fun d(tag: String = LOG_TAG, message: String) =
        log(tag, message, LogLevel.DEBUG)

    /**
     * Log info message
     */
    fun i(tag: String = LOG_TAG, message: String) =
        log(tag, message, LogLevel.INFO)

    /**
     * Log warning message
     */
    fun w(tag: String = LOG_TAG, message: String) =
        log(tag, message, LogLevel.WARNING)

    /**
     * Log error message with optional throwable
     */
    fun e(tag: String = LOG_TAG, message: String, throwable: Throwable? = null)
}

