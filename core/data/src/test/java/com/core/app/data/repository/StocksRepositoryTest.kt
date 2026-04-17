package com.core.app.data.repository

import arrow.core.Either
import com.core.common.error.DomainError
import com.core.data.repository.StocksRepository
import com.core.data.repository.StocksRepositoryImpl
import com.core.network.model.ApiError
import com.core.network.retrofit.StocksApi
import com.core.testing.data.stockChartResponse
import com.core.testing.data.stockOverviewResponse
import com.core.testing.data.tickersResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException

class StocksRepositoryTest {

    private lateinit var repository: StocksRepository

    private lateinit var getStocksApi: StocksApi

    @Before
    fun setUp() {
        getStocksApi = mockk()
        repository = StocksRepositoryImpl(stocksApi = getStocksApi)
    }

    @Test
    fun `getStockList returns mapped tickers on success`() = runTest {
        coEvery { getStocksApi.getStockList(null) } returns Either.Right(tickersResponse)

        val result = repository.getStockList(null)

        assertTrue(result.isRight())
        result.onRight {
            assertEquals(1, it.results.size)
            assertEquals("AAPL", it.results.first().ticker)
        }
    }

    @Test
    fun `getStockList returns HttpError on api http error`() = runTest {
        coEvery { getStocksApi.getStockList(null) } returns
            Either.Left(ApiError.HttpError(404, "Not Found", null))

        val result = repository.getStockList(null)

        assertTrue(result.isLeft())
        result.onLeft {
            assertTrue(it is DomainError.HttpError)
            assertEquals(404, (it as DomainError.HttpError).code)
        }
    }

    @Test
    fun `getStockList returns MissingNetworkConnection on network error`() = runTest {
        coEvery { getStocksApi.getStockList(null) } returns
            Either.Left(ApiError.NetworkError(UnknownHostException()))

        val result = repository.getStockList(null)

        assertTrue(result.isLeft())
        result.onLeft { assertTrue(it is DomainError.MissingNetworkConnection) }
    }

    @Test
    fun `searchStockByQuery returns mapped tickers on success`() = runTest {
        coEvery { getStocksApi.searchStockByQuery("AAPL", null) } returns Either.Right(
            tickersResponse
        )

        val result = repository.searchStockByQuery("AAPL", null)

        assertTrue(result.isRight())
        result.onRight { assertEquals("AAPL", it.results.first().ticker) }
    }

    @Test
    fun `searchStockByQuery returns error on api failure`() = runTest {
        coEvery { getStocksApi.searchStockByQuery("AAPL", null) } returns
            Either.Left(ApiError.HttpError(500, "Server Error", null))

        val result = repository.searchStockByQuery("AAPL", null)

        assertTrue(result.isLeft())
        result.onLeft { assertTrue(it is DomainError.HttpError) }
    }

    @Test
    fun `getStockOverviewByTicker returns mapped overview on success`() = runTest {
        coEvery { getStocksApi.getStockOverview("AAPL") } returns Either.Right(stockOverviewResponse)

        val result = repository.getStockOverviewByTicker("AAPL")

        assertTrue(result.isRight())
        result.onRight {
            assertEquals("AAPL", it.results.ticker)
            assertEquals("Apple Inc.", it.results.name)
        }
    }

    @Test
    fun `getStockOverviewByTicker returns error on api failure`() = runTest {
        coEvery { getStocksApi.getStockOverview("AAPL") } returns
            Either.Left(ApiError.UnknownError(RuntimeException("unexpected")))

        val result = repository.getStockOverviewByTicker("AAPL")

        assertTrue(result.isLeft())
        result.onLeft { assertTrue(it is DomainError.GeneralError) }
    }

    @Test
    fun `getStockChartData returns mapped chart on success`() = runTest {
        coEvery {
            getStocksApi.getStockChartData("AAPL", "2024-01-01", "2024-01-31", "day")
        } returns Either.Right(stockChartResponse)

        val result = repository.getStockChartData("AAPL", "2024-01-01", "2024-01-31", "day")

        assertTrue(result.isRight())
        result.onRight {
            assertEquals("AAPL", it.ticker)
            assertEquals(1, it.results.size)
            assertEquals(150.0, it.results.first().close, 0.0)
        }
    }

    @Test
    fun `getStockChartData returns error on api failure`() = runTest {
        coEvery {
            getStocksApi.getStockChartData("AAPL", "2024-01-01", "2024-01-31", "day")
        } returns Either.Left(ApiError.HttpError(429, "Rate limited", null))

        val result = repository.getStockChartData("AAPL", "2024-01-01", "2024-01-31", "day")

        assertTrue(result.isLeft())
        result.onLeft {
            assertTrue(it is DomainError.HttpError)
            assertEquals(429, (it as DomainError.HttpError).code)
        }
    }

}
