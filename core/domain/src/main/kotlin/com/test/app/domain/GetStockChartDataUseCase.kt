package com.test.app.domain

import com.test.app.common.dispatcher.DispatcherProvider
import com.test.app.common.result.DataResult
import com.test.app.common.result.asDataResult
import com.test.app.data.repository.StocksRepository
import com.test.app.model.data.StockChart
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class GetStockChartDataUseCase @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val stocksRepository: StocksRepository,
) {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun launch(ticker: String, period: String): Flow<DataResult<StockChart>> {
        val dateNow = LocalDate.now()
        val currentDate = dateNow.format(formatter)
        val dateMinusTwoYears = dateNow
            .minusYears(2)
            .format(formatter)

        return flow {
            emit(
                stocksRepository.getStockChartData(
                    ticker = ticker,
                    startDate = dateMinusTwoYears,
                    endDate = currentDate,
                    period = period,
                )
            )
        }
            .asDataResult()
            .flowOn(dispatcherProvider.io)
    }
}
