package com.feature.details.impl.domain.repository

import arrow.core.Either
import com.core.common.error.DomainError
import com.feature.details.impl.domain.model.StockChart
import com.feature.details.impl.domain.model.StockOverview

interface StocksDetailsRepository {

    suspend fun getStockOverviewByTicker(ticker: String): Either<DomainError, StockOverview>

    suspend fun getStockChartData(
        ticker: String,
        startDate: String,
        endDate: String,
        period: String,
    ): Either<DomainError, StockChart>

}
