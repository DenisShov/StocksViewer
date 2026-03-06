package com.test.app.data.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.test.app.data.repository.StocksRepository
import com.test.app.model.data.Ticker
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StocksSearchPager @Inject constructor(val repository: StocksRepository) {

    fun getPager(query: String): Flow<PagingData<Ticker>> = Pager(
        config = PagingConfig(pageSize = 2),
        pagingSourceFactory = {
            StocksPagingSource(repository, query)
        }).flow

}