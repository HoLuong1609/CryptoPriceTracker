package com.crypto.pricetracker.presentation.detail.trades

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradesScreen(
    viewModel: TradesViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val filterState by viewModel.filterState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.errorEvents) {
        viewModel.errorEvents.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    when (uiState) {
        TradesUiState.Loading -> {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is TradesUiState.Success -> {
            TradesContent(
                trades = (uiState as TradesUiState.Success).trades,
                currentFilter = filterState,
                onFilterChange = { viewModel.setFilter(it) },
                modifier = modifier
            )
        }
        is TradesUiState.Error -> {
            ErrorContent(
                message = (uiState as TradesUiState.Error).message,
                onRetry = { viewModel.retry() },
                modifier = modifier
            )
        }
    }
}

