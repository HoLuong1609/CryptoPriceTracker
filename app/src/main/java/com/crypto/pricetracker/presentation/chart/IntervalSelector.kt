package com.crypto.pricetracker.presentation.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.crypto.domain.model.KlineInterval

@Composable
fun IntervalSelector(
    selected: KlineInterval,
    onSelected: (KlineInterval) -> Unit,
    modifier: Modifier = Modifier
) {

    Row(modifier = modifier.padding(8.dp)) {

        KlineInterval.entries.forEach { interval ->

            val isSelected = interval == selected
            val backgroundColor = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }

            Text(
                text = interval.value,
                modifier = Modifier
                    .padding(4.dp)
                    .background(backgroundColor, shape = MaterialTheme.shapes.small)
                    .padding(8.dp)
                    .clickable {
                        onSelected(interval)
                    },
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}