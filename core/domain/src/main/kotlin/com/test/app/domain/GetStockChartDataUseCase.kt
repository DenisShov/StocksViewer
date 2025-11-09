package com.test.app.domain

import com.test.app.common.dispatcher.DispatcherProvider
import com.test.app.common.result.DataResult
import com.test.app.common.result.asDataResult
import com.test.app.data.repository.StocksRepository
import com.test.app.model.data.StockChart
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetStockChartDataUseCase @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val stocksRepository: StocksRepository,
) {
    fun launch(ticker: String): Flow<DataResult<StockChart>> {
        return flow {
            emit(stocksRepository.getStockChartData(ticker))
        }
            .asDataResult()
            .flowOn(dispatcherProvider.io)
    }
}
