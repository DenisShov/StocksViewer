package com.test.app.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import arrow.core.Either
import com.test.app.common.error.DomainError
import com.test.app.data.model.toDomain
import com.test.app.data.paging.StocksPagingSource
import com.test.app.data.util.mapLeftToDomainError
import com.test.app.model.data.StockChart
import com.test.app.model.data.StockOverview
import com.test.app.model.data.Ticker
import com.test.app.network.retrofit.StocksApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StocksRepositoryImpl @Inject constructor(
    private val stocksApi: StocksApi,
) : StocksRepository {

    override fun getStocksFlow(query: String): Flow<PagingData<Ticker>> = Pager(
        config = PagingConfig(pageSize = 2),
        pagingSourceFactory = {
            StocksPagingSource(stocksApi, query)
        }).flow

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
