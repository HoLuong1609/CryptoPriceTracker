package com.crypto.pricetracker.presentation.chart

import com.crypto.domain.model.Kline
import com.crypto.domain.model.KlineInterval

data class KlineUiState(
    val isLoading: Boolean = false,
    val klines: List<Kline> = emptyList(),
    val selectedInterval: KlineInterval = KlineInterval.FIFTEEN_MINUTES,
    val error: String? = null
)