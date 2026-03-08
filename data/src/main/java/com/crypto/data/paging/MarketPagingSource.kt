package com.crypto.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.crypto.domain.model.MarketCoin
import com.crypto.domain.usecase.GetMarketCoinsUseCase
import kotlinx.coroutines.flow.first

class MarketPagingSource(
    private val getMarketCoinsUseCase: GetMarketCoinsUseCase
) : PagingSource<Int, MarketCoin>() {

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, MarketCoin> {

        return try {

            val page = params.key ?: 0
            val pageSize = params.loadSize

            val coins = getMarketCoinsUseCase().first()

            val start = page * pageSize
            val end = (start + pageSize).coerceAtMost(coins.size)

            val subList = coins.subList(start, end)

            LoadResult.Page(
                data = subList,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (end == coins.size) null else page + 1
            )

        } catch (e: Exception) {

            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(
        state: PagingState<Int, MarketCoin>
    ): Int? {

        return state.anchorPosition
    }
}