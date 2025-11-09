package com.test.app.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.test.app.network.model.TickerResponse
import com.test.app.network.retrofit.StocksApi

class SearchStocksPagingSource(
    private val stocksApi: StocksApi,
    private val searchQuery: String,
) : PagingSource<String, TickerResponse>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, TickerResponse> {
        return try {
            val resp = stocksApi.searchStockByQuery(query = searchQuery, cursor = params.key)
            LoadResult.Page(
                data = resp.results,
                prevKey = null,
                nextKey = resp.nextUrl?.substringAfter(CURSOR_PARAMETER),
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<String, TickerResponse>): String? = null

    companion object {
        const val CURSOR_PARAMETER = "cursor="
    }
}