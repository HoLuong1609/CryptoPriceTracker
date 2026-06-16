package com.crypto.pricetracker.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.crypto.pricetracker.presentation.detail.chart.ChartContent
import com.crypto.pricetracker.presentation.detail.chart.KlineUiState
import com.crypto.pricetracker.presentation.detail.model.DetailTab
import com.crypto.pricetracker.presentation.detail.orderbook.OrderBookScreen
import com.crypto.pricetracker.presentation.detail.orderbook.OrderBookViewModel
import com.crypto.pricetracker.presentation.detail.trades.TradesScreen
import com.crypto.pricetracker.presentation.detail.trades.TradesViewModel
import com.crypto.pricetracker.ui.theme.ComposeColors
import com.crypto.domain.model.KlineInterval

/**
 * Content area for Coin Detail screen
 *
 * @param selectedTab Currently selected tab
 * @param tabs All available tabs
 * @param klineState State for chart tab
 * @param orderBookViewModel ViewModel for order book tab (hoisted from parent)
 * @param tradesViewModel ViewModel for trades tab (hoisted from parent)
 * @param onTabSelected Callback when tab is selected
 * @param onIntervalSelected Callback when chart interval is selected
 * @param modifier Optional modifier
 */
@Composable
fun CoinDetailContent(
    selectedTab: DetailTab,
    tabs: List<DetailTab>,
    klineState: KlineUiState,
    orderBookViewModel: OrderBookViewModel,
    tradesViewModel: TradesViewModel,
    onTabSelected: (DetailTab) -> Unit,
    onIntervalSelected: (KlineInterval) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ComposeColors.BackgroundSecondary)
    ) {
        // Tab row
        CoinDetailTabRow(
            tabs = tabs,
            selectedTab = selectedTab,
            onTabSelected = onTabSelected
        )

        // Tab content
        when (selectedTab) {
            DetailTab.Price -> {
                ChartContent(
                    klineState = klineState,
                    onIntervalSelected = onIntervalSelected
                )
            }
            DetailTab.OrderBook -> {
                OrderBookScreen(
                    viewModel = orderBookViewModel
                )
            }
            DetailTab.Trades -> {
                TradesScreen(
                    viewModel = tradesViewModel
                )
            }
        }
    }
}

