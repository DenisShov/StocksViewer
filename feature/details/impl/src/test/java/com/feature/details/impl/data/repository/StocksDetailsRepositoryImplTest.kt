package com.feature.details.impl.data.repository

import arrow.core.Either
import com.core.common.error.DomainError
import com.core.network.model.errors.ApiError
import com.core.network.retrofit.StocksApi
import com.core.testing.data.stockChartResponse
import com.core.testing.data.stockOverviewResponse
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

}
