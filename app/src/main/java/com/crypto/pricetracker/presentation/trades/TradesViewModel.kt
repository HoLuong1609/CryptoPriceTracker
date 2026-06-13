package com.crypto.pricetracker.presentation.trades

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crypto.domain.model.Trade
import com.crypto.domain.usecase.GetRecentTradesUseCase
import com.crypto.domain.usecase.ObserveTradesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Trades screen
 */
@HiltViewModel
class TradesViewModel @Inject constructor(
    private val getRecentTradesUseCase: GetRecentTradesUseCase,
    private val observeTradesUseCase: ObserveTradesUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val symbol: String = checkNotNull(savedStateHandle.get<String>("symbol")) {
        "Symbol is required"
    }

    private val _uiState = MutableStateFlow<TradesUiState>(TradesUiState.Loading)
    val uiState: StateFlow<TradesUiState> = _uiState.asStateFlow()

    private val _filterState = MutableStateFlow<TradeFilter>(TradeFilter.All)
    val filterState: StateFlow<TradeFilter> = _filterState.asStateFlow()

    private val _errorEvents = Channel<String>(Channel.BUFFERED)
    val errorEvents = _errorEvents.receiveAsFlow()

    init {
        loadTrades()
    }

    private fun loadTrades() {
        viewModelScope.launch {
            try {
                // Step 1: Load recent trades
                _uiState.value = TradesUiState.Loading
                val recentTrades = getRecentTradesUseCase(symbol, limit = 100)
                _uiState.value = TradesUiState.Success(recentTrades)

                // Step 2: Observe realtime stream
                observeTradesUseCase(symbol)
                    .catch { e ->
                        _errorEvents.send("Error: ${e.message}")
                    }
                    .combine(_filterState) { trades, filter ->
                        when (filter) {
                            TradeFilter.All -> trades
                            TradeFilter.Buys -> trades.filter { it.isBuy }
                            TradeFilter.Sells -> trades.filter { !it.isBuy }
                        }
                    }
                    .collect { trades ->
                        _uiState.value = TradesUiState.Success(trades)
                    }
            } catch (e: Exception) {
                _uiState.value = TradesUiState.Error(e.message ?: "Unknown error")
                _errorEvents.send(e.message ?: "Failed to load trades")
            }
        }
    }

    fun setFilter(filter: TradeFilter) {
        _filterState.value = filter
    }

    fun retry() {
        loadTrades()
    }
}

/**
 * UI State for Trades screen
 */
sealed class TradesUiState {
    object Loading : TradesUiState()
    data class Success(val trades: List<Trade>) : TradesUiState()
    data class Error(val message: String) : TradesUiState()
}

/**
 * Trade filter options
 */
enum class TradeFilter {
    All,
    Buys,
    Sells
}

