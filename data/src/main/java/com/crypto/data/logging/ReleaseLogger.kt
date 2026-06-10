package com.crypto.data.logging

import android.util.Log
import com.crypto.core.logging.Logger
import com.crypto.core.logging.LogLevel

/**
 * Release logger - only logs critical messages
 * Use in release builds to reduce log spam
 */
class ReleaseLogger : Logger {

    override fun log(tag: String, message: String, level: LogLevel) {
        // Only log ERROR and WARNING in release builds
        when (level) {
            LogLevel.ERROR -> Log.e(tag, message)
            LogLevel.WARNING -> Log.w(tag, message)
            else -> { /* Silent in release */ }
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

