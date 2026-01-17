package com.test.app.testing.repository

import androidx.paging.PagingData
import com.test.app.data.repository.StocksRepository
import com.test.app.model.data.StockChart
import com.test.app.model.data.StockOverview
import com.test.app.model.data.Ticker
import com.test.app.testing.data.testFlowPagingData
import com.test.app.testing.data.testStockChart
import com.test.app.testing.data.testStockDetails
import kotlinx.coroutines.flow.Flow

class TestStocksRepository : StocksRepository {

    override fun getStocksFlow(query: String): Flow<PagingData<Ticker>> {
        return testFlowPagingData
    }

    override suspend fun getStockOverviewByTicker(ticker: String): StockOverview {
        return testStockDetails
    }

    override suspend fun getStockChartData(
        ticker: String,
        startDate: String,
        endDate: String,
        period: String
    ): StockChart {
        return testStockChart
    }
}
