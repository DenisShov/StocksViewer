package com.feature.details.impl.data.repository

import arrow.core.Either
import com.core.common.error.DomainError
import com.core.network.model.errors.ApiError
import com.core.network.model.stocksDetails.CandleResponse
import com.core.network.model.stocksDetails.CompanyResponse
import com.core.network.model.stocksDetails.StockChartResponse
import com.core.network.model.stocksDetails.StockOverviewResponse
import com.core.network.retrofit.StocksApi
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class StocksDetailsRepositoryImplTest {

    private lateinit var repository: StocksDetailsRepositoryImpl

    private lateinit var getStocksApi: StocksApi

    @Before
    fun setUp() {
        getStocksApi = mockk()
        repository = StocksDetailsRepositoryImpl(stocksApi = getStocksApi)
    }

    @Test
    fun `getStockOverviewByTicker returns mapped overview on success`() = runTest {
        coEvery { getStocksApi.getStockOverview("AAPL") } returns Either.Right(stockOverviewResponse)

        val result = repository.getStockOverviewByTicker("AAPL")

        Assert.assertTrue(result.isRight())
        result.onRight {
            Assert.assertEquals("AAPL", it.results.ticker)
            Assert.assertEquals("Apple Inc.", it.results.name)
        }
    }

    @Test
    fun `getStockOverviewByTicker returns error on api failure`() = runTest {
        coEvery { getStocksApi.getStockOverview("AAPL") } returns
            Either.Left(ApiError.UnknownError(RuntimeException("unexpected")))

        val result = repository.getStockOverviewByTicker("AAPL")

        Assert.assertTrue(result.isLeft())
        result.onLeft { Assert.assertTrue(it is DomainError.GeneralError) }
    }

    @Test
    fun `getStockChartData returns mapped chart on success`() = runTest {
        coEvery {
            getStocksApi.getStockChartData("AAPL", "2024-01-01", "2024-01-31", "day")
        } returns Either.Right(stockChartResponse)

        val result = repository.getStockChartData("AAPL", "2024-01-01", "2024-01-31", "day")

        Assert.assertTrue(result.isRight())
        result.onRight {
            Assert.assertEquals("AAPL", it.ticker)
            Assert.assertEquals(1, it.results.size)
            Assert.assertEquals(150.0, it.results.first().close, 0.0)
        }
    }

    @Test
    fun `getStockChartData returns error on api failure`() = runTest {
        coEvery {
            getStocksApi.getStockChartData("AAPL", "2024-01-01", "2024-01-31", "day")
        } returns Either.Left(ApiError.HttpError(429, "Rate limited", null))

        val result = repository.getStockChartData("AAPL", "2024-01-01", "2024-01-31", "day")

        Assert.assertTrue(result.isLeft())
        result.onLeft {
            Assert.assertTrue(it is DomainError.HttpError)
            Assert.assertEquals(429, (it as DomainError.HttpError).code)
        }
    }

    val companyResponse = CompanyResponse(
        ticker = "AAPL",
        name = "Apple Inc.",
        market = "stocks",
        locale = "us",
        primaryExchange = "XNAS",
        type = "CS",
        active = true,
    )

    val stockOverviewResponse = StockOverviewResponse(
        requestId = "req1",
        results = companyResponse,
        status = "OK",
    )

    val candleResponse = CandleResponse(
        volume = 1000.0,
        vwap = 149.5,
        open = 148.0,
        close = 150.0,
        high = 151.0,
        low = 147.0,
        timestampMs = 1704067200000,
        transactions = 500,
    )

    val stockChartResponse = StockChartResponse(
        ticker = "AAPL",
        queryCount = 1,
        resultsCount = 1,
        adjusted = true,
        results = listOf(candleResponse),
        status = "OK",
        requestId = "req1",
        count = 1,
    )

}
