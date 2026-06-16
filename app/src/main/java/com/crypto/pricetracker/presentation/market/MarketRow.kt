package com.crypto.pricetracker.presentation.market

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crypto.core.util.CurrencyFormatter.formatPrice
import com.crypto.core.util.CurrencyFormatter.formatSymbol
import com.crypto.core.util.CurrencyFormatter.formatVndPrice
import com.crypto.core.util.CurrencyFormatter.formatVolume
import com.crypto.domain.model.MarketCoin
import com.crypto.pricetracker.ui.theme.ComposeColors
import com.crypto.pricetracker.ui.theme.ComposeColors.GreenUp
import com.crypto.pricetracker.ui.theme.ComposeColors.RedDown
import java.util.Locale

@Composable
fun MarketRow(
    coin: MarketCoin,
    onClick: () -> Unit
) {

    val change = coin.priceChangePercent
    val changeColor = if (change >= 0) GreenUp else RedDown
    val changePrefix = if (change >= 0) "+" else ""

    // Format symbol: BTCUSDT -> BTC/USDT
    val displaySymbol = formatSymbol(coin.symbol)
    // Extract base symbol for icon
    val baseSymbol = remember(coin.symbol) {
        val quoteAssets = listOf("USDT", "BUSD", "BTC", "ETH", "BNB")
        var result = coin.symbol
        for (quote in quoteAssets) {
            if (result.endsWith(quote) && result.length > quote.length) {
                result = result.removeSuffix(quote)
                break
            }
        }
        result
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Coin icon placeholder
        CoinIcon(
            symbol = coin.symbol,
            baseSymbol = baseSymbol
        )

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