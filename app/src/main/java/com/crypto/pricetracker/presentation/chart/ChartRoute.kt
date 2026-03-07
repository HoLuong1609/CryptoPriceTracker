package com.crypto.pricetracker.presentation.chart

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.crypto.domain.model.KlineInterval

@Composable
fun ChartRoute(
    symbol: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: KlineViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(symbol) {
        viewModel.loadKlines(symbol, KlineInterval.ONE_MINUTE)
    }

    ChartScreen(
        symbol = symbol,
        uiState = state,
        onBackClick = onBackClick,
        onIntervalSelected = { interval ->
            viewModel.changeInterval(symbol, interval)
        },
        modifier = modifier
    )
}
