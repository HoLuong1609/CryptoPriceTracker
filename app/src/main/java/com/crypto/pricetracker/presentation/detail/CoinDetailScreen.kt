package com.crypto.pricetracker.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.crypto.core.util.CurrencyFormatter.formatSymbol
import com.crypto.domain.model.KlineInterval
import com.crypto.pricetracker.presentation.chart.ChartContent
import com.crypto.pricetracker.presentation.chart.KlineViewModel
import com.crypto.pricetracker.presentation.orderbook.OrderBookScreen
import com.crypto.pricetracker.presentation.orderbook.OrderBookViewModel
import com.crypto.pricetracker.ui.theme.ComposeColors
import com.crypto.pricetracker.presentation.trades.TradesScreen
import com.crypto.pricetracker.presentation.trades.TradesViewModel

@Composable
fun CoinDetailScreen(
    symbol: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    klineViewModel: KlineViewModel = hiltViewModel()
) {
    val klineState by klineViewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }

    // Hoist ViewModels here so they survive tab switches
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ComposeColors.BackgroundPrimary)
                .padding(horizontal = 4.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = ComposeColors.TextPrimary
                )
            }
            Text(
                text = displaySymbol,
                color = ComposeColors.TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Tab row
        val tabs = listOf("Price", "Order Book", "Trades")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ComposeColors.BackgroundPrimary)
        ) {
            tabs.forEachIndexed { index, title ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedTab = index }
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = title,
                        color = if (selectedTab == index) ComposeColors.TextPrimary else ComposeColors.TextSecondary,
                        fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(2.dp)
                            .background(
                                if (selectedTab == index) ComposeColors.AccentYellow else Color.Transparent
                            )
                    )
                }
            }
        }

        // Tab content
        when (selectedTab) {
            0 -> ChartContent(
                klineState = klineState,
                onIntervalSelected = { klineViewModel.changeInterval(it) }
            )
            1 -> OrderBookScreen(
                viewModel = orderBookViewModel
            )
            2 -> TradesScreen(
                viewModel = tradesViewModel
            )
        }
    }
}