package com.core.data.repository

import arrow.core.Either
import com.core.model.StockChart
import com.core.model.StockOverview
import com.core.model.Tickers
import com.core.common.error.DomainError

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
