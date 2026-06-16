package com.crypto.pricetracker.presentation.detail.model

/**
 * Sealed class representing tabs in Coin Detail screen
 * This is UI-level model
 */
sealed class DetailTab(
    val title: String,
    val index: Int
) {
    data object Price : DetailTab("Price", 0)
    data object OrderBook : DetailTab("Order Book", 1)
    data object Trades : DetailTab("Trades", 2)

    companion object {
        /**
         * All available tabs in display order
         */
        fun all(): List<DetailTab> = listOf(Price, OrderBook, Trades)

        /**
         * Get tab by index
         */
        fun fromIndex(index: Int): DetailTab = all().getOrElse(index) { Price }
    }
}

