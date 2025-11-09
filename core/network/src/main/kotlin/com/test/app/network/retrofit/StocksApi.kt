package com.test.app.network.retrofit

import com.test.app.network.BuildConfig
import com.test.app.network.model.StockChartResponse
import com.test.app.network.model.StockOverviewResponse
import com.test.app.network.model.TickersResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StocksApi {

    @GET("v3/reference/tickers?market=stocks&search={query}&active=true&limit=50&apiKey=${BuildConfig.API_KEY}")
    suspend fun searchStockByQuery(
        @Path("query") query: String, @Query("cursor") cursor: String? = null
    ): TickersResponse

    @GET("v3/reference/tickers?market=stocks&active=true&limit=50&apiKey=${BuildConfig.API_KEY}")
    suspend fun getStockList(@Query("cursor") cursor: String? = null): TickersResponse

    @GET("v3/reference/tickers/{ticker}?apiKey=${BuildConfig.API_KEY}")
    suspend fun getStockOverview(@Path("ticker") ticker: String): StockOverviewResponse

    @GET("v2/aggs/ticker/{ticker}/range/1/day/2023-01-01/2025-09-28?adjusted=true&sort=asc&limit=50000&apiKey=${BuildConfig.API_KEY}")
    suspend fun getStockChartData(@Path("ticker") ticker: String): StockChartResponse
}
