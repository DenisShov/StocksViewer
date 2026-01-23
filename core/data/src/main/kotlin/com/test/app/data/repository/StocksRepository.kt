package com.test.app.data.repository

import androidx.paging.PagingData
import arrow.core.Either
import com.test.app.common.error.DomainError
import com.test.app.model.data.StockChart
import com.test.app.model.data.StockOverview
import com.test.app.model.data.Ticker
import com.test.app.network.model.ApiError
import kotlinx.coroutines.flow.Flow

interface StocksRepository {

    fun getStocksFlow(query: String): Flow<PagingData<Ticker>>

    suspend fun getStockOverviewByTicker(ticker: String): Either<DomainError, StockOverview>

    suspend fun getStockChartData(
        ticker: String,
        startDate: String,
        endDate: String,
        period: String
    ): Either<DomainError, StockChart>
}
