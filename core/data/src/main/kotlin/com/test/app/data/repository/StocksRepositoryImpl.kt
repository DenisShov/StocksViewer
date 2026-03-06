package com.test.app.data.repository

import arrow.core.Either
import com.test.app.common.error.DomainError
import com.test.app.data.model.toDomain
import com.test.app.data.utils.mapLeftToDomainError
import com.test.app.model.data.StockChart
import com.test.app.model.data.StockOverview
import com.test.app.model.data.Tickers
import com.test.app.network.retrofit.StocksApi
import javax.inject.Inject

class StocksRepositoryImpl @Inject constructor(
    private val stocksApi: StocksApi,
) : StocksRepository {

    override suspend fun searchStockByQuery(
        searchQuery: String,
        cursor: String?
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
        period: String
    ): Either<DomainError, StockChart> =
        stocksApi.getStockChartData(ticker, startDate, endDate, period)
            .mapLeftToDomainError()
            .map { it.toDomain() }
}
