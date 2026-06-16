package com.crypto.pricetracker.presentation.market

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crypto.pricetracker.R
import com.crypto.pricetracker.ui.theme.ComposeColors

@Composable
fun MarketHeaderRow() {
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