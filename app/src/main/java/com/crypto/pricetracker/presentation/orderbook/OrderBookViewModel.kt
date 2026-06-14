package com.crypto.pricetracker.presentation.orderbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crypto.domain.model.OrderBook
import com.crypto.domain.usecase.GetOrderBookUseCase
import com.crypto.domain.usecase.ObserveOrderBookUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for Orderbook screen
 */
@HiltViewModel(assistedFactory = OrderBookViewModel.Factory::class)
class OrderBookViewModel @AssistedInject constructor(
    private val getOrderBookUseCase: GetOrderBookUseCase,
    private val observeOrderBookUseCase: ObserveOrderBookUseCase,
    @Assisted private val symbol: String
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(symbol: String): OrderBookViewModel
    }

    private val _uiState = MutableStateFlow<OrderBookUiState>(OrderBookUiState.Loading)
    val uiState: StateFlow<OrderBookUiState> = _uiState.asStateFlow()

    private val _errorEvents = Channel<String>(Channel.BUFFERED)
    val errorEvents = _errorEvents.receiveAsFlow()

    init {
        loadOrderBook()
    }

    private fun loadOrderBook() {
        viewModelScope.launch {
            try {
                // Step 1: Load snapshot
                _uiState.value = OrderBookUiState.Loading
                val snapshot = getOrderBookUseCase(symbol, limit = 20)
                _uiState.value = OrderBookUiState.Success(snapshot)

                // Step 2: Observe realtime updates
                observeOrderBookUseCase(symbol)
                    .catch { e ->
                        _errorEvents.send("Error: ${e.message}")
                    }
                    .collect { orderBook ->
                        _uiState.value = OrderBookUiState.Success(orderBook)
                    }
            } catch (e: Exception) {
                _uiState.value = OrderBookUiState.Error(e.message ?: "Unknown error")
                _errorEvents.send(e.message ?: "Failed to load orderbook")
            }
        }
    }

    fun retry() {
        loadOrderBook()
    }
}

/**
 * UI State for Orderbook screen
 */
sealed class OrderBookUiState {
    object Loading : OrderBookUiState()
    data class Success(val orderBook: OrderBook) : OrderBookUiState()
    data class Error(val message: String) : OrderBookUiState()
}


