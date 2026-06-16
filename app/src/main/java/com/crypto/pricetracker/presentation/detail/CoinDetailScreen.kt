package com.crypto.pricetracker.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.crypto.core.util.CurrencyFormatter.formatSymbol
import com.crypto.domain.model.KlineInterval
import com.crypto.pricetracker.presentation.detail.chart.KlineViewModel
import com.crypto.pricetracker.presentation.detail.components.CoinDetailActionBar
import com.crypto.pricetracker.presentation.detail.components.CoinDetailContent
import com.crypto.pricetracker.presentation.detail.model.DetailTab
import com.crypto.pricetracker.presentation.detail.orderbook.OrderBookViewModel
import com.crypto.pricetracker.presentation.detail.trades.TradesViewModel
import com.crypto.pricetracker.ui.theme.ComposeColors

/**
 * Coin Detail Screen - Detail view for a specific cryptocurrency
 *
 * @param symbol Trading pair symbol (e.g., "BTCUSDT")
 * @param onBackClick Callback when back button is clicked
 * @param modifier Optional modifier
 * @param klineViewModel ViewModel for chart data
 */
@Composable
fun CoinDetailScreen(
    symbol: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    klineViewModel: KlineViewModel = hiltViewModel()
) {
    val klineState by klineViewModel.uiState.collectAsState()

    var selectedTab by remember { mutableStateOf<DetailTab>(DetailTab.Price) }

    val orderBookViewModel: OrderBookViewModel = hiltViewModel<OrderBookViewModel, OrderBookViewModel.Factory> { factory ->
        factory.create(symbol)
    }
    val tradesViewModel: TradesViewModel = hiltViewModel<TradesViewModel, TradesViewModel.Factory> { factory ->
        factory.create(symbol)
    }

    val displaySymbol = formatSymbol(symbol)

    LaunchedEffect(symbol) {
        klineViewModel.loadKlines(symbol, KlineInterval.FIFTEEN_MINUTES)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ComposeColors.BackgroundSecondary)
            .statusBarsPadding()
    ) {
        // Action bar
        CoinDetailActionBar(
            symbol = displaySymbol,
            onBackClick = onBackClick
        )

        // Content area
        CoinDetailContent(
            selectedTab = selectedTab,
            tabs = DetailTab.all(),
            klineState = klineState,
            orderBookViewModel = orderBookViewModel,
            tradesViewModel = tradesViewModel,
            onTabSelected = { selectedTab = it },
            onIntervalSelected = { klineViewModel.changeInterval(it) }
        )
    }
}