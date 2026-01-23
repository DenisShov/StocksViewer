package com.test.app.domain

import arrow.core.Either
import com.test.app.common.error.DomainError
import com.test.app.data.repository.StocksRepository
import com.test.app.model.data.StockChart
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class GetStockChartDataUseCase @Inject constructor(
    private val stocksRepository: StocksRepository,
) {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    suspend fun launch(ticker: String, period: String): Either<DomainError, StockChart> {
        val dateNow = LocalDate.now()
        val currentDate = dateNow.format(formatter)
        val dateMinusTwoYears = dateNow
            .minusYears(2)
            .format(formatter)

        return stocksRepository.getStockChartData(
            ticker = ticker,
            startDate = dateMinusTwoYears,
            endDate = currentDate,
            period = period,
        )
    }
}
