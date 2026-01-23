package com.test.app.domain

import arrow.core.Either
import com.test.app.common.error.DomainError
import com.test.app.data.repository.StocksRepository
import com.test.app.model.data.StockOverview
import javax.inject.Inject

class GetStockOverviewByTickerUseCase @Inject constructor(
    private val stocksRepository: StocksRepository,
) {
    suspend fun launch(ticker: String): Either<DomainError, StockOverview> =
        stocksRepository.getStockOverviewByTicker(ticker)

}
