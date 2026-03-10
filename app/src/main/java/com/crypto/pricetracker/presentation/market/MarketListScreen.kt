package com.crypto.pricetracker.presentation.market

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.crypto.domain.model.MarketCoin
import com.crypto.pricetracker.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketListScreen(
    modifier: Modifier = Modifier,
    onMarketClick: (String) -> Unit,
    viewModel: MarketViewModel = hiltViewModel(),
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.errorEvents) {
        viewModel.errorEvents.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.market_list)) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier
    ) { innerPadding ->

        val markets = viewModel.markets.collectAsLazyPagingItems()

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {

            items(markets.itemCount) { index ->
                markets[index]?.let {
                    MarketRow(coin = it, onClick = { onMarketClick(it.symbol) })
                }
            }

        }
    }
}

@Composable
private fun MarketRow(
    coin: MarketCoin,
    onClick: () -> Unit
) {

    val change = coin.priceChangePercent
    val color = if (change >= 0) Color(0xFF16C784) else Color(0xFFEA3943)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
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