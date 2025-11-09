package com.test.app.data.repository

import androidx.paging.PagingData
import com.test.app.model.data.StockChart
import com.test.app.model.data.StockOverview
import com.test.app.model.data.Ticker
import kotlinx.coroutines.flow.Flow

interface StocksRepository {

    fun getStocksFlow(): Flow<PagingData<Ticker>>

    suspend fun getStockOverviewByTicker(ticker: String): StockOverview

    suspend fun getStockChartData(ticker: String): StockChart
}
