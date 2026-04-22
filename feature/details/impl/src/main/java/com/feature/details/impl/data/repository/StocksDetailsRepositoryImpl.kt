package com.feature.details.impl.data.repository

import arrow.core.Either
import com.core.common.error.DomainError
import com.core.network.model.errors.mapLeftToDomainError
import com.core.network.retrofit.StocksApi
import com.feature.details.impl.domain.mapper.toDomain
import com.feature.details.impl.domain.model.StockChart
import com.feature.details.impl.domain.model.StockOverview
import com.feature.details.impl.domain.repository.StocksDetailsRepository
import javax.inject.Inject

class StocksDetailsRepositoryImpl @Inject constructor(
    private val stocksApi: StocksApi,
) : StocksDetailsRepository {

    override suspend fun getStockOverviewByTicker(ticker: String): Either<DomainError, StockOverview> =
        stocksApi.getStockOverview(ticker)
            .mapLeftToDomainError()
            .map { it.toDomain() }

    override suspend fun getStockChartData(
        ticker: String,
        startDate: String,
        endDate: String,
        period: String,
    ): Either<DomainError, StockChart> =
        stocksApi.getStockChartData(ticker, startDate, endDate, period)
            .mapLeftToDomainError()
            .map { it.toDomain() }
}
