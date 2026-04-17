package com.core.data.repository

import arrow.core.Either
import com.core.common.error.DomainError
import com.core.data.model.toDomain
import com.core.data.utils.mapLeftToDomainError
import com.core.model.StockChart
import com.core.model.StockOverview
import com.core.model.Tickers
import com.core.network.retrofit.StocksApi
import javax.inject.Inject

class StocksRepositoryImpl @Inject constructor(
    private val stocksApi: StocksApi,
) : StocksRepository {

    override suspend fun searchStockByQuery(
        searchQuery: String,
        cursor: String?,
    ): Either<DomainError, Tickers> =
        stocksApi.searchStockByQuery(searchQuery = searchQuery, cursor = cursor)
            .mapLeftToDomainError()
            .map { it.toDomain() }

    override suspend fun getStockList(cursor: String?): Either<DomainError, Tickers> =
        stocksApi.getStockList(cursor = cursor)
            .mapLeftToDomainError()
            .map { it.toDomain() }

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
