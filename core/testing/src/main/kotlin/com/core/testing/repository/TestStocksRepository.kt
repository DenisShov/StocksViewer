package com.core.testing.repository

import arrow.core.Either
import arrow.core.right
import com.core.common.error.DomainError
import com.core.data.repository.StocksRepository
import com.core.model.StockChart
import com.core.model.StockOverview
import com.core.model.Tickers
import com.core.testing.data.testStockChart
import com.core.testing.data.testStockDetails
import com.core.testing.data.testTickers

class TestStocksRepository : StocksRepository {

    override suspend fun getStockOverviewByTicker(ticker: String): Either<DomainError, StockOverview> {
        return testStockDetails.right()
    }

    override suspend fun getStockChartData(
        ticker: String,
        startDate: String,
        endDate: String,
        period: String
    ): Either<DomainError, StockChart> {
        return testStockChart.right()
    }

    override suspend fun getStockList(cursor: String?): Either<DomainError, Tickers> {
        return testTickers.right()
    }

    override suspend fun searchStockByQuery(
        searchQuery: String,
        cursor: String?
    ): Either<DomainError, Tickers> {
        return testTickers.right()
    }

}