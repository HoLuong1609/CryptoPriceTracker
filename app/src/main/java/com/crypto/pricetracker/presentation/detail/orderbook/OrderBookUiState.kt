package com.crypto.pricetracker.presentation.detail.orderbook

import com.crypto.domain.model.OrderBook

/**
 * UI State for Orderbook screen
 */
sealed class OrderBookUiState {
    object Loading : OrderBookUiState()
    data class Success(val orderBook: OrderBook) : OrderBookUiState()
    data class Error(val message: String) : OrderBookUiState()
}