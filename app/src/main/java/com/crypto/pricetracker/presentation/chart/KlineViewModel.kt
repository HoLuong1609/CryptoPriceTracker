package com.crypto.pricetracker.presentation.chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crypto.core.result.AppResult
import com.crypto.domain.model.KlineInterval
import com.crypto.domain.usecase.GetKlinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KlineViewModel @Inject constructor(
    private val getKlinesUseCase: GetKlinesUseCase
) : ViewModel() {

    companion object {
        private const val DEFAULT_CANDLE_COUNT = 100
    }

    private val _uiState = MutableStateFlow(KlineUiState())
    val uiState: StateFlow<KlineUiState> = _uiState.asStateFlow()

    fun changeInterval(
        symbol: String,
        interval: KlineInterval
    ) {

        _uiState.value = _uiState.value.copy(
            selectedInterval = interval
        )

        loadKlines(symbol, interval)
    }

    fun loadKlines(symbol: String, interval: KlineInterval) {

        viewModelScope.launch {

            val endTime = System.currentTimeMillis()
            val startTime = calculateStartTime(endTime, interval)

            getKlinesUseCase(
                symbol = symbol,
                interval = interval.value,
                startTime = startTime,
                endTime = endTime
            ).collect { result ->

                when (result) {

                    is AppResult.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = true
                        )
                    }

                    is AppResult.Success -> {
                        _uiState.value = _uiState.value.copy(
                            klines = result.data,
                            isLoading = false,
                            error = null
                        )
                    }

                    is AppResult.Error -> {
                        _uiState.value = _uiState.value.copy(
                            error = result.message,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    private fun calculateStartTime(
        endTime: Long,
        interval: KlineInterval
    ): Long {

        val candleMillis = when(interval) {

            KlineInterval.ONE_MINUTE -> 60_000L
            KlineInterval.THREE_MINUTES -> 180_000L
            KlineInterval.FIVE_MINUTES -> 300_000L
            KlineInterval.FIFTEEN_MINUTES -> 900_000L
            KlineInterval.THIRTY_MINUTES -> 1_800_000L
            KlineInterval.ONE_HOUR -> 3_600_000L
            KlineInterval.FOUR_HOURS -> 14_400_000L
        }

        return endTime - candleMillis * DEFAULT_CANDLE_COUNT
    }
}