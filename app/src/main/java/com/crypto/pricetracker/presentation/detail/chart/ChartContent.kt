package com.crypto.pricetracker.presentation.detail.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crypto.core.util.CurrencyFormatter.formatPrice
import com.crypto.core.util.CurrencyFormatter.formatVndPrice
import com.crypto.domain.model.KlineInterval
import com.crypto.pricetracker.ui.theme.ComposeColors
import java.util.Locale

@Composable
fun ChartContent(
    klineState: KlineUiState,
    onIntervalSelected: (KlineInterval) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ComposeColors.BackgroundSecondary)
    ) {
        // Price info row
        val lastKline = klineState.klines.lastOrNull()
        val price = lastKline?.closePrice ?: 0.0
        val high24h = klineState.klines.maxOfOrNull { it.highPrice } ?: 0.0
        val low24h = klineState.klines.minOfOrNull { it.lowPrice } ?: 0.0
        val openPrice = klineState.klines.firstOrNull()?.openPrice ?: 0.0
        val changePercent = if (openPrice > 0) ((price - openPrice) / openPrice) * 100 else 0.0
        val changeColor = if (changePercent >= 0) ComposeColors.GreenUp else ComposeColors.RedDown
        val changePrefix = if (changePercent >= 0) "+" else ""

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Price + VND + Change%
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = formatPrice(price),
                    color = ComposeColors.TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = formatVndPrice(price),
                        color = ComposeColors.TextSecondary,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$changePrefix${String.format(Locale.US, "%.2f", changePercent)}%",
                        color = changeColor,
                        fontSize = 12.sp
                    )
                }
            }

            // 24h High
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Text(
                    text = "24h High",
                    color = ComposeColors.TextSecondary,
                    fontSize = 11.sp
                )
                Text(
                    text = formatPrice(high24h),
                    color = ComposeColors.TextPrimary,
                    fontSize = 12.sp
                )
            }

            // 24h Low
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "24h Low",
                    color = ComposeColors.TextSecondary,
                    fontSize = 11.sp
                )
                Text(
                    text = formatPrice(low24h),
                    color = ComposeColors.TextPrimary,
                    fontSize = 12.sp
                )
            }
        }

        // Time interval filter row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ComposeColors.BackgroundPrimary)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            KlineInterval.entries.forEach { interval ->
                val isSelected = interval == klineState.selectedInterval
                Text(
                    text = interval.value.uppercase(),
                    color = if (isSelected) ComposeColors.AccentYellow else ComposeColors.TextSecondary,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .clickable { onIntervalSelected(interval) }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        }

        // Chart
        when {
            klineState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = ComposeColors.AccentYellow)
                }
            }
            klineState.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = klineState.error ?: "",
                        color = ComposeColors.RedDown
                    )
                }
            }
            klineState.klines.isNotEmpty() -> {
                CandleStickChartView(
                    klines = klineState.klines,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }
    }
}