package com.crypto.pricetracker.presentation.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.crypto.data.paging.MarketPagingSource
import com.crypto.domain.model.MarketCoin
import com.crypto.domain.usecase.GetMarketCoinsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val getMarketCoinsUseCase: GetMarketCoinsUseCase
) : ViewModel() {

    val markets: Flow<PagingData<MarketCoin>> = Pager(

        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),

        pagingSourceFactory = {

            MarketPagingSource(getMarketCoinsUseCase)

        }

    ).flow.cachedIn(viewModelScope)
}