package com.crypto.pricetracker.presentation.orderbook

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crypto.core.util.CurrencyFormatter
import com.crypto.domain.model.OrderBook
import com.crypto.pricetracker.R
import com.crypto.pricetracker.ui.theme.ComposeColors

@Composable
fun OrderBookContent(
    orderBook: OrderBook,
    modifier: Modifier = Modifier
) {
    val spreadText = CurrencyFormatter.formatSpreadValue(orderBook.spread)
    val spreadPercentText = CurrencyFormatter.formatSpreadPercentage(orderBook.spreadPercentage)

    Column(modifier = modifier
        .fillMaxSize()
        .background(ComposeColors.BackgroundSecondary)) {
        // Spread info
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ComposeColors.BackgroundPrimary)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.spread, spreadText),
                color = ComposeColors.TextPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
            Text(
                text = stringResource(R.string.spread_percentage, spreadPercentText),
                color = ComposeColors.TextSecondary,
                fontSize = 12.sp
            )
        }

        // Column headers
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text("Price", color = ComposeColors.TextSecondary, fontSize = 11.sp, modifier = Modifier.weight(1f))
            Text("Qty", color = ComposeColors.TextSecondary, fontSize = 11.sp, textAlign = TextAlign.End, modifier = Modifier.weight(1f))
            Spacer(Modifier.width(16.dp))
            Text("Price", color = ComposeColors.TextSecondary, fontSize = 11.sp, modifier = Modifier.weight(1f))
            Text("Qty", color = ComposeColors.TextSecondary, fontSize = 11.sp, textAlign = TextAlign.End, modifier = Modifier.weight(1f))
        }

        // Asks | Bids side by side
        Row(modifier = Modifier.fillMaxSize()) {
            // Asks (sell)
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(
                    count = minOf(orderBook.asks.size, 20),
                    key = { index -> "ask_$index" }
                ) { index ->
                    PriceLevelItem(orderBook.asks[index], isAsk = true)
                }
            }

            // Divider
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(Color(0xFF2B3139))
            )

            // Bids (buy)
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(
                    count = minOf(orderBook.bids.size, 20),
                    key = { index -> "bid_$index" }
                ) { index ->
                    PriceLevelItem(orderBook.bids[index], isAsk = false)
                }
            }
        }
    }
}