package com.crypto.pricetracker.presentation.trades

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.crypto.domain.model.Trade
import com.crypto.pricetracker.R
import com.crypto.pricetracker.ui.theme.ComposeColors

@Composable
fun TradesContent(
    trades: List<Trade>,
    currentFilter: TradeFilter,
    onFilterChange: (TradeFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Filter chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = currentFilter == TradeFilter.All,
                onClick = { onFilterChange(TradeFilter.All) },
                label = { Text(stringResource(R.string.all)) },
                colors = FilterChipDefaults.filterChipColors(
                    labelColor = ComposeColors.TextPrimary
                )
            )
            FilterChip(
                selected = currentFilter == TradeFilter.Buys,
                onClick = { onFilterChange(TradeFilter.Buys) },
                label = { Text(stringResource(R.string.buys)) },
                colors = FilterChipDefaults.filterChipColors(
                    labelColor = ComposeColors.TextPrimary
                )
            )
            FilterChip(
                selected = currentFilter == TradeFilter.Sells,
                onClick = { onFilterChange(TradeFilter.Sells) },
                label = { Text(stringResource(R.string.sells)) },
                colors = FilterChipDefaults.filterChipColors(
                    labelColor = ComposeColors.TextPrimary
                )
            )
        }

        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

        // Trades list
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = trades,
                key = { it.id }
            ) { trade ->
                TradeItem(trade)
            }
        }
    }
}