package com.crypto.pricetracker.presentation.market

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.crypto.domain.model.MarketCoin

@Composable
fun MarketListScreen(
    viewModel: MarketViewModel = hiltViewModel()
) {

    val markets = viewModel.markets.collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {

        items(markets.itemCount) { index ->
            markets[index]?.let {
                MarketRow(it)
            }
        }

    }
}

@Composable
private fun MarketRow(
    coin: MarketCoin
) {

    val change = coin.priceChangePercent
    val color = if (change >= 0) Color(0xFF16C784) else Color(0xFFEA3943)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = coin.symbol,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = "$${coin.price}",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = "${coin.priceChangePercent}%",
            color = color,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}