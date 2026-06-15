package com.crypto.pricetracker.presentation.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.crypto.core.logging.Logger
import com.crypto.domain.extension.withResilience
import com.crypto.domain.model.MarketCoin
import com.crypto.domain.usecase.GetMarketCoinsUseCase
import com.crypto.domain.usecase.GetPagedMarketCoinsUseCase
import com.crypto.domain.usecase.ObserveNetworkStatusUseCase
import com.crypto.domain.usecase.StartTickerUpdatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val getMarketCoinsUseCase: GetMarketCoinsUseCase,
    private val startTickerUpdates: StartTickerUpdatesUseCase,
    private val getPagedMarketCoinsUseCase: GetPagedMarketCoinsUseCase,
    private val observeNetworkStatusUseCase: ObserveNetworkStatusUseCase,
    private val logger: Logger
) : ViewModel() {

    companion object {
        private const val TAG = "MarketViewModel"
    }

    private val _errorEvents = Channel<String>(Channel.BUFFERED)
    val errorEvents = _errorEvents.receiveAsFlow()

    private var observingJob: Job? = null

    private var hasInitializedOnce = false  // Prevent re-fetching on resume

    val markets: Flow<PagingData<MarketCoin>> =
        getPagedMarketCoinsUseCase()
            .cachedIn(viewModelScope)
            .also { logger.d(TAG, "markets Flow created and cached") }

    fun startObserving() {
        if (observingJob?.isActive == true) {
            logger.d(TAG, "Already observing, skip")
            return
        }
        logger.d(TAG, "startObserving() - Resume WebSocket")
        observeNetwork()
    }

    fun stopObserving() {
        logger.d(TAG, "stopObserving() - Pause WebSocket")
        observingJob?.cancel()
        observingJob = null
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeNetwork() {
        logger.d(TAG, "Starting observeNetwork()...")
        observingJob = observeNetworkStatusUseCase().flatMapLatest { isOnline ->
            logger.d(TAG, "Network status changed: isOnline=$isOnline")
            if (isOnline) {
                logger.i(TAG, "Network online - starting market data streams")

                val streams = if (!hasInitializedOnce) {
                    hasInitializedOnce = true
                    merge(
                        getMarketCoinsUseCase(),
                        startTickerUpdates()
                    )
                } else {
                    startTickerUpdates()
                }
                streams.withResilience(
                    bufferSize = 100,
                    timeout = 8.seconds
                )
                .catch { e ->
                    logger.e(TAG, "Error in market streams: ${e.message}", e)
                    _errorEvents.send(e.message ?: "An unexpected error occurred")
                    // Don't re-throw - keep stream alive for recovery
                }
            } else {
                logger.i(TAG, "Network offline")
                _errorEvents.send("No internet connection")
                emptyFlow()
            }
        }
            .launchIn(viewModelScope)
            .also { logger.d(TAG, "observeNetwork stream launched") }
    }

    override fun onCleared() {
        super.onCleared()
        stopObserving()
    }
}