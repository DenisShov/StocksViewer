package com.feature.details.impl.domain.usecase

import arrow.core.left
import arrow.core.right
import com.core.common.error.DomainError
import com.feature.details.impl.domain.model.Candle
import com.feature.details.impl.domain.model.StockChart
import com.feature.details.impl.domain.repository.StocksDetailsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class GetStockChartDataUseCaseTest {

    private val repository: StocksDetailsRepository = mockk()
    private lateinit var underTest: GetStockChartDataUseCase

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    @Before
    fun setup() {
        coEvery {
            repository.getStockChartData(any(), any(), any(), any())
        } returns testStockChart.right()

        underTest = GetStockChartDataUseCase(stocksDetailsRepository = repository)
    }

    @Test
    fun launch_thenPassesCorrectTicker() = runTest {
        underTest.launch("AAPL", "week")

        coVerify {
            repository.getStockChartData(
                ticker = "AAPL",
                startDate = any(),
                endDate = any(),
                period = any(),
            )
        }
    }

    @Test
    fun launch_thenStartDateIsTwoYearsAgo() = runTest {
        val startDateSlot = slot<String>()
        coEvery {
            repository.getStockChartData(any(), capture(startDateSlot), any(), any())
        } returns testStockChart.right()

        underTest.launch("AAPL", "week")

        val expectedStartDate = LocalDate.now().minusYears(2).format(formatter)
        assertEquals(expectedStartDate, startDateSlot.captured)
    }

    @Test
    fun launch_thenEndDateIsToday() = runTest {
        val endDateSlot = slot<String>()
        coEvery {
            repository.getStockChartData(any(), any(), capture(endDateSlot), any())
        } returns testStockChart.right()

        underTest.launch("AAPL", "week")

        val expectedEndDate = LocalDate.now().format(formatter)
        assertEquals(expectedEndDate, endDateSlot.captured)
    }

    @Test
    fun launch_thenPassesCorrectPeriod() = runTest {
        underTest.launch("AAPL", "month")

        coVerify {
            repository.getStockChartData(
                ticker = any(),
                startDate = any(),
                endDate = any(),
                period = "month",
            )
        }
    }

    @Test
    fun launch_whenNetworkError_thenReturnsLeftWithError() = runTest {
        coEvery {
            repository.getStockChartData(any(), any(), any(), any())
        } returns DomainError.MissingNetworkConnection.left()

        val result = underTest.launch("AAPL", "week")

        assertTrue(result.isLeft())
        result.onLeft { error ->
            assertIs<DomainError.MissingNetworkConnection>(error)
        }
    }

    @Test
    fun launch_whenGeneralError_thenReturnsLeftWithGeneralError() = runTest {
        val exception = RuntimeException("Unexpected failure")
        coEvery {
            repository.getStockChartData(any(), any(), any(), any())
        } returns DomainError.GeneralError(exception).left()

        val result = underTest.launch("AAPL", "week")

        assertTrue(result.isLeft())
        result.onLeft { error ->
            assertIs<DomainError.GeneralError>(error)
            assertEquals("Unexpected failure", error.exception.message)
        }
    }

    val testCandles = listOf(
        Candle(
            volume = 50_000_000.0,
            vwap = 185.5,
            open = 185.82,
            close = 184.8,
            high = 186.03,
            low = 184.21,
            timestampMs = 1699851600000,
            transactions = 500000,
        ),
        Candle(
            volume = 45_000_000.0,
            vwap = 187.0,
            open = 187.7,
            close = 187.44,
            high = 188.11,
            low = 186.3,
            timestampMs = 1699938000000,
            transactions = 450000,
        ),
    )

    val testStockChart = StockChart(
        ticker = "AAPL",
        queryCount = 2,
        resultsCount = 2,
        adjusted = true,
        results = testCandles,
        status = "OK",
        requestId = "test-request-id",
        count = 2,
    )

}
