package com.crypto.pricetracker.presentation.market

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.crypto.core.theme.BinanceColors.ACCENT_YELLOW
import com.crypto.core.theme.BinanceColors.BACKGROUND_SECONDARY
import com.crypto.core.theme.BinanceColors.SURFACE
import com.crypto.core.util.CurrencyFormatter.formatPrice
import com.crypto.core.util.CurrencyFormatter.formatSymbol
import com.crypto.core.util.CurrencyFormatter.formatVndPrice
import com.crypto.core.util.CurrencyFormatter.formatVolume
import com.crypto.domain.model.MarketCoin
import com.crypto.pricetracker.R
import com.crypto.pricetracker.ui.theme.ComposeColors
import com.crypto.pricetracker.ui.theme.ComposeColors.GreenUp
import com.crypto.pricetracker.ui.theme.ComposeColors.RedDown
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketListScreen(
    modifier: Modifier = Modifier,
    onMarketClick: (String) -> Unit,
    viewModel: MarketViewModel = hiltViewModel(),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(viewModel.errorEvents) {
        viewModel.errorEvents.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .background(ComposeColors.BackgroundPrimary)
                    .statusBarsPadding()
            ) {
                // App title
                Text(
                    text = stringResource(R.string.market_list),
                    color = ComposeColors.TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )

                // Search bar
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.search_hint),
                            color = ComposeColors.TextSecondary,
                            fontSize = 14.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = ComposeColors.TextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color(SURFACE),
                        focusedContainerColor = Color(SURFACE),
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        cursorColor = Color(ACCENT_YELLOW),
                        focusedTextColor = ComposeColors.TextPrimary,
                        unfocusedTextColor = ComposeColors.TextPrimary
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 12.dp)
                        .height(44.dp)
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(BACKGROUND_SECONDARY),
        modifier = modifier
    ) { innerPadding ->

        val markets = viewModel.markets.collectAsLazyPagingItems()

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

@Composable
private fun MarketHeaderRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Name / Vol - left
        Text(
            text = stringResource(R.string.header_name_vol),
            color = ComposeColors.TextSecondary,
            fontSize = 11.sp,
            modifier = Modifier.weight(1f)
        )

        // Last Price - right aligned with price column
        Text(
            text = stringResource(R.string.header_last_price),
            color = ComposeColors.TextSecondary,
            fontSize = 11.sp,
            textAlign = TextAlign.End,
            modifier = Modifier.width(100.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // 24h Chg% - right
        Text(
            text = stringResource(R.string.header_24h_chg),
            color = ComposeColors.TextSecondary,
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(72.dp)
        )
    }
}

@Composable
private fun MarketRow(
    coin: MarketCoin,
    onClick: () -> Unit
) {

    val change = coin.priceChangePercent
    val changeColor = if (change >= 0) GreenUp else RedDown
    val changePrefix = if (change >= 0) "+" else ""

    // Format symbol: BTCUSDT -> BTC/USDT
    val displaySymbol = formatSymbol(coin.symbol)
    // Extract base symbol for icon
    val baseSymbol = coin.symbol.removeSuffix("USDT").removeSuffix("BTC").removeSuffix("ETH").removeSuffix("BNB")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Coin icon placeholder
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(0xFF2B3139)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = baseSymbol.take(1),
                color = Color(0xFFF0B90B),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Name + Volume column
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = displaySymbol,
                color = ComposeColors.TextPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = formatVolume(coin.volume),
                color = ComposeColors.TextSecondary,
                fontSize = 11.sp
            )
        }

        // Price column (USDT + VND)
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.width(100.dp)
        ) {
            Text(
                text = formatPrice(coin.price),
                color = ComposeColors.TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 1
            )
            Text(
                text = formatVndPrice(coin.price),
                color = ComposeColors.TextSecondary,
                fontSize = 11.sp,
                maxLines = 1
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Change % badge
        Box(
            modifier = Modifier
                .width(72.dp)
                .height(32.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(changeColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$changePrefix${String.format(Locale.US, "%.2f", change)}%",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp
            )
        }
    }
}