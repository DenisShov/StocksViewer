package com.test.app.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.test.app.data.model.asExternalModel
import com.test.app.model.data.Ticker
import com.test.app.network.retrofit.StocksApi
import javax.inject.Inject

class StocksPagingSource @Inject constructor(
    private val stocksApi: StocksApi
) : PagingSource<String, Ticker>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Ticker> {
        return try {
            val resp = stocksApi.getStockList(cursor = params.key)
            LoadResult.Page(
                data = resp.results.map { it.asExternalModel() },
                prevKey = null,
                nextKey = resp.nextUrl?.substringAfter(CURSOR_PARAMETER),
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<String, Ticker>): String? = null

    companion object {
        const val CURSOR_PARAMETER = "cursor="
    }
}
