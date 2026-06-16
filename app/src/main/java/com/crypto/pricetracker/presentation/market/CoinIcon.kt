package com.crypto.pricetracker.presentation.market

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.crypto.core.theme.BinanceColors.ACCENT_YELLOW
import com.crypto.core.theme.BinanceColors.SURFACE

@Composable
fun CoinIcon(
    symbol: String,
    baseSymbol: String,
    modifier: Modifier = Modifier
) {

    val coinSymbol = baseSymbol.lowercase()
    val iconUrl = "https://assets.coincap.io/assets/icons/$coinSymbol@2x.png"

    var imageLoadState by remember { mutableStateOf<AsyncImagePainter.State>(AsyncImagePainter.State.Empty) }

    Box(
        modifier = modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(Color(SURFACE)),
        contentAlignment = Alignment.Center
    ) {
        // Always try to load the image
        AsyncImage(
            model = iconUrl,
            contentDescription = symbol,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape),
            onState = { state ->
                imageLoadState = state
            },
            contentScale = ContentScale.Crop
        )

        // Show fallback text if image is loading or failed
        if (imageLoadState !is AsyncImagePainter.State.Success) {
            Text(
                text = baseSymbol.take(3).uppercase(),
                color = Color(ACCENT_YELLOW),
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            )
        }
    }
}