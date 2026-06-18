package com.crypto.pricetracker.presentation.market

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.paging.compose.LazyPagingItems
import com.crypto.core.theme.BinanceColors.BACKGROUND_SECONDARY
import com.crypto.domain.model.MarketCoin
import com.crypto.domain.model.TickerUpdate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketListContent(
    markets: LazyPagingItems<MarketCoin>,
    tickerUpdates: Map<String, TickerUpdate>,
    onMarketClick: (String) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {

    var searchQuery by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            MarketTopBar(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it }
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
            item(key = "header") {
                MarketHeaderRow()
            }

            // Filter applied here to avoid PagingData.filter() which would recreate entire list
            items(
                count = markets.itemCount,
                key = { index -> markets.peek(index)?.symbol ?: "item_$index" }
            ) { index ->
                markets[index]?.let { coin ->
                    // Apply filter check
                    val matchesSearch = if (searchQuery.isBlank()) {
                        true
                    } else {
                        val baseAsset = extractBaseAsset(coin.symbol)
                        baseAsset.startsWith(searchQuery, ignoreCase = true)
                    }

                    if (matchesSearch) {
                        // Merge database data with realtime updates
                        val update = tickerUpdates[coin.symbol]
                        val mergedCoin = if (update != null) {
                            coin.copy(
                                price = update.price,
                                priceChangePercent = update.change
                            )
                        } else {
                            coin
                        }

                        MarketRow(
                            coin = mergedCoin,
                            onClick = { onMarketClick(coin.symbol) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Extract base asset from symbol by removing quote asset suffix
 */
private fun extractBaseAsset(symbol: String): String {
    val quoteAssets = listOf("USDT", "BUSD", "BTC", "ETH", "BNB")
    for (quote in quoteAssets) {
        if (symbol.endsWith(quote) && symbol.length > quote.length) {
            return symbol.removeSuffix(quote)
        }
    }
    return symbol
}