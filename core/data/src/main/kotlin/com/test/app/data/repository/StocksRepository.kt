package com.test.app.data.repository

import arrow.core.Either
import com.test.app.common.error.DomainError
import com.test.app.model.data.StockChart
import com.test.app.model.data.StockOverview
import com.test.app.model.data.Tickers

interface StocksRepository {

    suspend fun getStockOverviewByTicker(ticker: String): Either<DomainError, StockOverview>

    suspend fun getStockChartData(
        ticker: String,
        startDate: String,
        endDate: String,
        period: String
    ): Either<DomainError, StockChart>

    suspend fun getStockList(cursor: String?): Either<DomainError, Tickers>

    suspend fun searchStockByQuery(
        searchQuery: String,
        cursor: String?
    ): Either<DomainError, Tickers>
}
