package com.crypto.pricetracker.presentation.market

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.paging.compose.collectAsLazyPagingItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketListScreen(
    modifier: Modifier = Modifier,
    onMarketClick: (String) -> Unit,
    viewModel: MarketViewModel = hiltViewModel(),
) {
    var searchQuery by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current
    val markets = viewModel.markets.collectAsLazyPagingItems()

    // Lifecycle-aware WebSocket management
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    viewModel.startObserving()
                }
                Lifecycle.Event.ON_PAUSE -> {
                    viewModel.stopObserving()
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Side effect: Show error messages
    LaunchedEffect(viewModel.errorEvents) {
        viewModel.errorEvents.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    // Delegate rendering to pure UI component
    MarketListContent(
        markets = markets,
        onMarketClick = onMarketClick,
        searchQuery = searchQuery,
        onSearchQueryChange = { searchQuery = it },
        snackbarHostState = snackbarHostState,
        modifier = modifier
    )
}