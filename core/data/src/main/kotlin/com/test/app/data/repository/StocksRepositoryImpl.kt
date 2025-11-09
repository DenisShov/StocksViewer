package com.test.app.data.repository

import androidx.paging.Pager
import com.test.app.data.model.asExternalModel
import com.test.app.model.data.StockChart
import com.test.app.model.data.StockOverview
import com.test.app.model.data.Ticker
import com.test.app.network.retrofit.StocksApi
import javax.inject.Inject

class StocksRepositoryImpl @Inject constructor(
    private val stocksApi: StocksApi, private val pager: Pager<String, Ticker>
) : StocksRepository {

    override fun getStocksFlow() = pager.flow

    override suspend fun getStockOverviewByTicker(ticker: String): StockOverview =
        stocksApi.getStockOverview(ticker).asExternalModel()

    override suspend fun getStockChartData(ticker: String): StockChart =
        stocksApi.getStockChartData(ticker).asExternalModel()

}
