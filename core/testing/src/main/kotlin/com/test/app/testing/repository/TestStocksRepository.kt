package com.test.app.testing.repository

import arrow.core.Either
import arrow.core.right
import com.test.app.common.error.DomainError
import com.test.app.data.repository.StocksRepository
import com.test.app.model.data.StockChart
import com.test.app.model.data.StockOverview
import com.test.app.model.data.Tickers
import com.test.app.testing.data.testStockChart
import com.test.app.testing.data.testStockDetails
import com.test.app.testing.data.testTickers

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
