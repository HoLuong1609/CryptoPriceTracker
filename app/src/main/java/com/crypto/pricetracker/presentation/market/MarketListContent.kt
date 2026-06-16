package com.crypto.pricetracker.presentation.market

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.paging.compose.LazyPagingItems
import com.crypto.core.theme.BinanceColors.BACKGROUND_SECONDARY
import com.crypto.domain.model.MarketCoin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketListContent(
    markets: LazyPagingItems<MarketCoin>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onMarketClick: (String) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            MarketTopBar(
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(BACKGROUND_SECONDARY),
        modifier = modifier
    ) { innerPadding ->


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Header row
            item {
                MarketHeaderRow()
            }

            items(markets.itemCount) { index ->
                markets[index]?.let {
                    MarketRow(coin = it, onClick = { onMarketClick(it.symbol) })
                }
            }
        }
    }
}