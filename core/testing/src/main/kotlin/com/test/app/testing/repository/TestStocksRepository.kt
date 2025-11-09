package com.test.app.testing.repository

import androidx.paging.PagingData
import com.test.app.data.repository.StocksRepository
import com.test.app.model.data.StockDetails
import com.test.app.model.data.StockOverview
import com.test.app.testing.data.testStockDetails
import com.test.app.testing.data.testFlowPagingData
import kotlinx.coroutines.flow.Flow

class TestStocksRepository : StocksRepository {

    override fun getStocksFlow(): Flow<PagingData<StockOverview>> {
        return testFlowPagingData
    }

    override suspend fun getStockOverviewByTicker(ticker: String): StockDetails {
        return testStockDetails
    }
}
