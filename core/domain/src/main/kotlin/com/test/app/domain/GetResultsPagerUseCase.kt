package com.test.app.domain

import androidx.paging.PagingData
import com.test.app.data.paging.StocksSearchPager
import com.test.app.model.data.Ticker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class GetResultsPagerUseCase @Inject constructor(
    private val stocksSearchPager: StocksSearchPager,
) {

    fun execute(query: String): Flow<PagingData<Ticker>> =
        stocksSearchPager.getPager(query)
            .distinctUntilChanged()

}
