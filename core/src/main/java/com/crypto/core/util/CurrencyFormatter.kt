package com.crypto.core.util

import java.text.NumberFormat
import java.util.Locale

/**
 * Utility object for formatting currency and trading symbols
 */
object CurrencyFormatter {

    /**
     * Format trading symbol by separating base and quote assets
     * Example: BTCUSDT -> BTC/USDT
     */
    fun formatSymbol(symbol: String): String {
        for (quote in Constants.QUOTE_ASSETS) {
            if (symbol.endsWith(quote) && symbol.length > quote.length) {
                return "${symbol.removeSuffix(quote)}/$quote"
            }
        }
        return symbol
    }

    /**
     * Format volume with appropriate suffix (K, M, B)
     */
    fun formatVolume(volume: Double): String {
        return when {
            volume >= 1_000_000_000 -> String.format(Locale.US, "%.2fB", volume / 1_000_000_000)
            volume >= 1_000_000 -> String.format(Locale.US, "%.2fM", volume / 1_000_000)
            volume >= 1_000 -> String.format(Locale.US, "%.2fK", volume / 1_000)
            else -> String.format(Locale.US, "%.2f", volume)
        }
    }

    /**
     * Format price with appropriate decimal places based on magnitude
     */
    fun formatPrice(price: Double): String {
        return when {
            price >= 1_000 -> String.format(Locale.US, "%,.2f", price)
            price >= 1 -> String.format(Locale.US, "%.2f", price)
            price >= 0.01 -> String.format(Locale.US, "%.4f", price)
            else -> String.format(Locale.US, "%.8f", price)
        }
    }

    /**
     * Format USD price to VND with appropriate formatting
     */
    fun formatVndPrice(priceUsd: Double): String {
        val vnd = priceUsd * Constants.USD_TO_VND

        return when {
            // For very small USD prices (< $0.01), show VND with decimals
            priceUsd < 0.01 -> {
                when {
                    vnd >= 1 -> String.format(Locale.US, "%.2fđ", vnd)
                    vnd >= 0.01 -> String.format(Locale.US, "%.4fđ", vnd)
                    else -> String.format(Locale.US, "%.6fđ", vnd)
                }
            }
            // For normal prices (>= $0.01), format as integer VND
            else -> {
                val nf = NumberFormat.getIntegerInstance(Locale("vi", "VN"))
                "${nf.format(vnd.toLong())}đ"
            }
        }
    }
}

