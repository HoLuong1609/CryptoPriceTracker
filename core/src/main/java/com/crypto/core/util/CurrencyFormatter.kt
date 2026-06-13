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

    /**
     * Format quantity with appropriate decimal places
     */
    fun formatQuantity(quantity: Double): String {
        return when {
            quantity >= 1_000 -> String.format(Locale.US, "%,.2f", quantity)
            quantity >= 1 -> String.format(Locale.US, "%.4f", quantity)
            quantity >= 0.001 -> String.format(Locale.US, "%.6f", quantity)
            else -> String.format(Locale.US, "%.8f", quantity)
        }
    }

    /**
     * Format spread value with dynamic precision based on magnitude
     * Used in OrderBook to show precise spread amounts
     */
    fun formatSpreadValue(spreadValue: Double): String {
        return when {
            spreadValue >= 1.0 -> String.format(Locale.US, "%.2f", spreadValue)
            spreadValue >= 0.01 -> String.format(Locale.US, "%.4f", spreadValue)
            else -> String.format(Locale.US, "%.6f", spreadValue)
        }
    }

    /**
     * Format spread percentage with dynamic precision based on magnitude
     * Used in OrderBook to show precise spread percentage
     */
    fun formatSpreadPercentage(spreadPercent: Double): String {
        return when {
            spreadPercent >= 0.1 -> String.format(Locale.US, "%.3f", spreadPercent)
            spreadPercent >= 0.01 -> String.format(Locale.US, "%.4f", spreadPercent)
            else -> String.format(Locale.US, "%.6f", spreadPercent)
        }
    }
}

