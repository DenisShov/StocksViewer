package com.core.domain

import arrow.core.Either
import com.core.common.error.DomainError
import com.core.data.repository.StocksRepository
import com.core.model.StockChart
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
