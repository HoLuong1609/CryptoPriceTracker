package com.crypto.pricetracker.ui.theme

import androidx.compose.ui.graphics.Color
import com.crypto.core.theme.BinanceColors

/**
 * Compose Color wrappers for BinanceColors constants.
 */
object ComposeColors {

    // Background Colors
    /** Primary background - Dark surface (#1E2329) */
    val BackgroundPrimary = Color(BinanceColors.BACKGROUND_PRIMARY)

    /** Secondary background - Slightly lighter (#181A20) */
    val BackgroundSecondary = Color(BinanceColors.BACKGROUND_SECONDARY)

    /** Card/container surface background (#2B3139) */
    val Surface = Color(BinanceColors.SURFACE)

    // Text Colors
    /** Primary text - Light gray (#EAECEF) */
    val TextPrimary = Color(BinanceColors.TEXT_PRIMARY)

    /** Secondary text - Medium gray (#848E9C) */
    val TextSecondary = Color(BinanceColors.TEXT_SECONDARY)

    // Price Change Colors
    /** Green - Price up (#0ECB81) */
    val GreenUp = Color(BinanceColors.GREEN_UP)

    /** Red - Price down (#F6465D) */
    val RedDown = Color(BinanceColors.RED_DOWN)

    // Accent Colors
    /** Binance yellow - Brand color (#F0B90B) */
    val AccentYellow = Color(BinanceColors.ACCENT_YELLOW)
}