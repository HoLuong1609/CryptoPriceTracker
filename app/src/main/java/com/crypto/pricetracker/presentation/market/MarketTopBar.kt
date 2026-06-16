package com.crypto.pricetracker.presentation.market

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crypto.core.theme.BinanceColors.ACCENT_YELLOW
import com.crypto.core.theme.BinanceColors.SURFACE
import com.crypto.pricetracker.R
import com.crypto.pricetracker.ui.theme.ComposeColors

/**
 * Top bar component for Market List screen
 * Contains title and search functionality
 */
@Composable
fun MarketTopBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
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
            onValueChange = onSearchQueryChange,
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
                .defaultMinSize(minHeight = 48.dp),
            textStyle = TextStyle(fontSize = 14.sp)
        )
    }
}

