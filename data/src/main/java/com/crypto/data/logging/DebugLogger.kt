package com.crypto.data.logging

import android.util.Log
import com.crypto.core.logging.Logger
import com.crypto.core.logging.LogLevel

/**
 * Debug logger - logs everything
 * Use in debug builds for comprehensive logging
 */
class DebugLogger : Logger {

    override fun log(tag: String, message: String, level: LogLevel) {
        val logMessage = "[$level] $message"
        when (level) {
            LogLevel.DEBUG -> Log.d(tag, logMessage)
            LogLevel.INFO -> Log.i(tag, logMessage)
            LogLevel.WARNING -> Log.w(tag, logMessage)
            LogLevel.ERROR -> Log.e(tag, logMessage)
        }
    }

    override fun e(tag: String, message: String, throwable: Throwable?) {
        if (throwable != null) {
            Log.e(tag, message, throwable)
        } else {
            Log.e(tag, message)
        }
    }
}

