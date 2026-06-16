package com.crypto.pricetracker.presentation.detail.orderbook

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crypto.domain.model.PriceLevel
import com.crypto.pricetracker.ui.theme.ComposeColors
import java.util.Locale

@Composable
fun PriceLevelItem(level: PriceLevel, isAsk: Boolean) {
    val priceColor = if (isAsk) ComposeColors.RedDown else ComposeColors.GreenUp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = String.format(Locale.US, "%.2f", level.price),
            color = priceColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = String.format(Locale.US, "%.4f", level.quantity),
            color = ComposeColors.TextSecondary,
            fontSize = 12.sp
        )
    }
}