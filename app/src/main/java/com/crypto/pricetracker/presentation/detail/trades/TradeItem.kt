package com.crypto.pricetracker.presentation.detail.trades

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crypto.core.util.CurrencyFormatter.formatPrice
import com.crypto.core.util.CurrencyFormatter.formatQuantity
import com.crypto.core.util.DateTimeUtils.formatTime
import com.crypto.domain.model.Trade
import com.crypto.pricetracker.ui.theme.ComposeColors
import com.crypto.pricetracker.ui.theme.ComposeColors.GreenUp
import com.crypto.pricetracker.ui.theme.ComposeColors.RedDown

/**
 * Trade item - Shows single executed trade
 */
@Composable
fun TradeItem(trade: Trade) {
    // Green for buy, Red for sell
    val sideColor = if (trade.isBuy) GreenUp else RedDown

    // Format price based on value
    val formattedPrice = formatPrice(trade.price)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = formatTime(trade.time),
            color = ComposeColors.TextSecondary,
            fontSize = 12.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = formattedPrice,
            color = sideColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = formatQuantity(trade.quantity),
            color = ComposeColors.TextPrimary,
            fontSize = 12.sp,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}