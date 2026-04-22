package com.feature.list.impl.ui.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.core.common.error.DomainError
import com.feature.list.impl.domain.model.Ticker
import com.feature.list.impl.domain.repository.StocksListRepository

class StocksPagingSource(
    private val repository: StocksListRepository,
    private val query: String,
) : PagingSource<String, Ticker>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Ticker> {
        return if (query.isNotEmpty()) {
            repository.searchStockByQuery(searchQuery = query, cursor = params.key)
        } else {
            repository.getStockList(cursor = params.key)
        }.fold(
            ifLeft = { error ->
                LoadResult.Error(throwable = mapToSearchResultError(error))
            },
            ifRight = { tickers ->
                LoadResult.Page(
                    data = tickers.results,
                    prevKey = null,
                    nextKey = tickers.nextUrl?.substringAfter(CURSOR_PARAMETER),
                )
            }
        )
    }

    override fun getRefreshKey(state: PagingState<String, Ticker>): String? = null

    private fun mapToSearchResultError(error: DomainError): SearchResultsError = when (error) {
        is DomainError.MissingNetworkConnection -> SearchResultsError.NetworkError

        is DomainError.HttpError -> SearchResultsError.HttpError(errorMessage = error.message)

        else -> SearchResultsError.UnknownError
    }

    companion object {
        const val CURSOR_PARAMETER = "cursor="
    }
}
