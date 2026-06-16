package com.crypto.pricetracker.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crypto.pricetracker.presentation.detail.model.DetailTab
import com.crypto.pricetracker.ui.theme.ComposeColors

/**
 * Tab row for switching between Price/OrderBook/Trades
 *
 * @param tabs List of tabs to display
 * @param selectedTab Currently selected tab
 * @param onTabSelected Callback when tab is clicked
 * @param modifier Optional modifier
 */
@Composable
fun CoinDetailTabRow(
    tabs: List<DetailTab>,
    selectedTab: DetailTab,
    onTabSelected: (DetailTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(ComposeColors.BackgroundPrimary)
    ) {
        tabs.forEach { tab ->
            val isSelected = selectedTab == tab
            
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onTabSelected(tab) }
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = tab.title,
                    color = if (isSelected) ComposeColors.TextPrimary else ComposeColors.TextSecondary,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(2.dp)
                        .background(
                            if (isSelected) ComposeColors.AccentYellow else Color.Transparent
                        )
                )
            }
        }
    }
}

