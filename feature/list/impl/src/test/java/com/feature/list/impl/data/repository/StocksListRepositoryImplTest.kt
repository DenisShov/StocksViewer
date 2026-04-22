package com.feature.list.impl.data.repository

import arrow.core.Either
import com.core.common.error.DomainError
import com.core.network.model.errors.ApiError
import com.core.network.retrofit.StocksApi
import com.core.testing.data.tickersResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException

class StocksListRepositoryImplTest {

    private lateinit var repository: StocksListRepositoryImpl

    private lateinit var getStocksApi: StocksApi

    @Before
    fun setUp() {
        getStocksApi = mockk()
        repository = StocksListRepositoryImpl(stocksApi = getStocksApi)
    }

    @Test
    fun `getStockList returns mapped tickers on success`() = runTest {
        coEvery { getStocksApi.getStockList(null) } returns Either.Right(tickersResponse)

        val result = repository.getStockList(null)

        Assert.assertTrue(result.isRight())
        result.onRight {
            Assert.assertEquals(1, it.results.size)
            Assert.assertEquals("AAPL", it.results.first().ticker)
        }
    }

    @Test
    fun `getStockList returns HttpError on api http error`() = runTest {
        coEvery { getStocksApi.getStockList(null) } returns
            Either.Left(ApiError.HttpError(404, "Not Found", null))

        val result = repository.getStockList(null)

        Assert.assertTrue(result.isLeft())
        result.onLeft {
            Assert.assertTrue(it is DomainError.HttpError)
            Assert.assertEquals(404, (it as DomainError.HttpError).code)
        }
    }

    @Test
    fun `getStockList returns MissingNetworkConnection on network error`() = runTest {
        coEvery { getStocksApi.getStockList(null) } returns
            Either.Left(ApiError.NetworkError(UnknownHostException()))

        val result = repository.getStockList(null)

        Assert.assertTrue(result.isLeft())
        result.onLeft { Assert.assertTrue(it is DomainError.MissingNetworkConnection) }
    }

    @Test
    fun `searchStockByQuery returns mapped tickers on success`() = runTest {
        coEvery { getStocksApi.searchStockByQuery("AAPL", null) } returns Either.Right(
            tickersResponse
        )

        val result = repository.searchStockByQuery("AAPL", null)

        Assert.assertTrue(result.isRight())
        result.onRight { Assert.assertEquals("AAPL", it.results.first().ticker) }
    }

    @Test
    fun `searchStockByQuery returns error on api failure`() = runTest {
        coEvery { getStocksApi.searchStockByQuery("AAPL", null) } returns
            Either.Left(ApiError.HttpError(500, "Server Error", null))

        val result = repository.searchStockByQuery("AAPL", null)

        Assert.assertTrue(result.isLeft())
        result.onLeft { Assert.assertTrue(it is DomainError.HttpError) }
    }

}
