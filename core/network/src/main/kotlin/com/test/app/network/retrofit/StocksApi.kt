package com.test.app.network.retrofit

import arrow.core.Either
import com.test.app.network.BuildConfig
import com.test.app.network.model.ApiError
import com.test.app.network.model.StockChartResponse
import com.test.app.network.model.StockOverviewResponse
import com.test.app.network.model.TickersResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StocksApi {

    @GET("v3/reference/tickers?market=stocks&active=true&limit=50&apiKey=${BuildConfig.API_KEY}")
    suspend fun searchStockByQuery(
        @Query("search") searchQuery: String,
        @Query("cursor") cursor: String? = null
    ): Either<ApiError, TickersResponse>

    @GET("v3/reference/tickers?market=stocks&active=true&limit=50&apiKey=${BuildConfig.API_KEY}")
    suspend fun getStockList(
        @Query("cursor") cursor: String? = null
    ): Either<ApiError, TickersResponse>

    @GET("v3/reference/tickers/{ticker}?apiKey=${BuildConfig.API_KEY}")
    suspend fun getStockOverview(
        @Path("ticker") ticker: String
    ): Either<ApiError, StockOverviewResponse>

    @GET("v2/aggs/ticker/{ticker}/range/1/{period}/{startDate}/{endDate}?adjusted=true&sort=asc&limit=50000&apiKey=${BuildConfig.API_KEY}")
    suspend fun getStockChartData(
        @Path("ticker") ticker: String,
        @Path("startDate") startDate: String,
        @Path("endDate") endDate: String,
        @Path("period") period: String
    ): Either<ApiError, StockChartResponse>
}
