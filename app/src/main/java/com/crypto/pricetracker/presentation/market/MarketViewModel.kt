package com.crypto.pricetracker.presentation.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.crypto.domain.model.MarketCoin
import com.crypto.domain.usecase.GetMarketCoinsUseCase
import com.crypto.domain.usecase.GetPagedMarketCoinsUseCase
import com.crypto.domain.usecase.ObserveNetworkStatusUseCase
import com.crypto.domain.usecase.StartTickerUpdatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val getMarketCoinsUseCase: GetMarketCoinsUseCase,
    private val startTickerUpdates: StartTickerUpdatesUseCase,
    private val getPagedMarketCoinsUseCase: GetPagedMarketCoinsUseCase,
    private val observeNetworkStatusUseCase: ObserveNetworkStatusUseCase
) : ViewModel() {

    private val _errorEvents = Channel<String>(Channel.BUFFERED)
    val errorEvents = _errorEvents.receiveAsFlow()

    init {
        observeNetwork()
    }

    val markets: Flow<PagingData<MarketCoin>> =
        getPagedMarketCoinsUseCase()
            .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeNetwork() {
        observeNetworkStatusUseCase().flatMapLatest { isOnline ->
            if (isOnline) {
                merge(
                    getMarketCoinsUseCase(),
                    startTickerUpdates()
                ).catch { e ->
                    _errorEvents.send(e.message ?: "An unexpected error occurred")
                }
            } else {
                _errorEvents.send("No internet connection")
                emptyFlow()
            }
        }
            .launchIn(viewModelScope)
    }
}