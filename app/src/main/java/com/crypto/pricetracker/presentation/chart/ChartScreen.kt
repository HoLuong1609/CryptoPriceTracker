package com.crypto.pricetracker.presentation.chart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.crypto.domain.model.KlineInterval
import com.crypto.pricetracker.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartScreen(
    symbol: String,
    uiState: KlineUiState,
    onBackClick: () -> Unit,
    onIntervalSelected: (KlineInterval) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = symbol) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            IntervalSelector(
                selected = uiState.selectedInterval,
                onSelected = onIntervalSelected,
                modifier = Modifier
            )

            Spacer(modifier = Modifier.height(8.dp))

            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier)
                }

                uiState.error != null -> {
                    Text(
                        text = stringResource(R.string.error_loading_chart, uiState.error),
                        modifier = Modifier
                    )
                }

                else -> {
                    KlineChart(
                        klines = uiState.klines,
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    )
                }
            }
        }
    }
}
