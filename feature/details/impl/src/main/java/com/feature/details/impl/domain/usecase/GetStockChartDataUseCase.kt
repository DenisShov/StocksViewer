package com.feature.details.impl.domain.usecase

import arrow.core.Either
import com.core.common.error.DomainError
import com.feature.details.impl.domain.model.StockChart
import com.feature.details.impl.domain.repository.StocksDetailsRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
class GetStockChartDataUseCase(
    private val stocksDetailsRepository: StocksDetailsRepository,
) {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    suspend fun launch(ticker: String, period: String): Either<DomainError, StockChart> {
        val dateNow = LocalDate.now()
        val currentDate = dateNow.format(formatter)
        val dateMinusTwoYears = dateNow
            .minusYears(2)
            .format(formatter)

        return stocksDetailsRepository.getStockChartData(
            ticker = ticker,
            startDate = dateMinusTwoYears,
            endDate = currentDate,
            period = period,
        )
    }
}
