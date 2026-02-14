package com.test.app.domain

import androidx.paging.PagingData
import com.test.app.data.repository.StocksRepository
import com.test.app.model.data.Ticker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class GetResultsPagerUseCase @Inject constructor(
    private val stocksRepository: StocksRepository,
) {

    fun execute(query: String): Flow<PagingData<Ticker>> = stocksRepository
        .getStocksFlow(query)
        .distinctUntilChanged()

}
