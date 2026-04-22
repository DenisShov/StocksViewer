package com.feature.list.impl.ui.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.feature.list.impl.domain.model.Ticker
import com.feature.list.impl.domain.repository.StocksListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StocksSearchPager @Inject constructor(val repository: StocksListRepository) {

    fun getPager(query: String): Flow<PagingData<Ticker>> = Pager(
        config = PagingConfig(pageSize = 2),
        pagingSourceFactory = {
            StocksPagingSource(repository, query)
        }).flow

}
