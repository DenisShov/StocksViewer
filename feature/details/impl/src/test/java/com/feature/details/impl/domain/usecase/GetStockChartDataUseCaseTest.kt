package com.feature.details.impl.domain.usecase

import arrow.core.left
import arrow.core.right
import com.core.common.error.DomainError
import com.core.testing.data.TEST_TICKER
import com.core.testing.data.testStockChart
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
        underTest.launch(TEST_TICKER, "week")

        coVerify {
            repository.getStockChartData(
                ticker = TEST_TICKER,
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

        underTest.launch(TEST_TICKER, "week")

        val expectedStartDate = LocalDate.now().minusYears(2).format(formatter)
        assertEquals(expectedStartDate, startDateSlot.captured)
    }

    @Test
    fun launch_thenEndDateIsToday() = runTest {
        val endDateSlot = slot<String>()
        coEvery {
            repository.getStockChartData(any(), any(), capture(endDateSlot), any())
        } returns testStockChart.right()

        underTest.launch(TEST_TICKER, "week")

        val expectedEndDate = LocalDate.now().format(formatter)
        assertEquals(expectedEndDate, endDateSlot.captured)
    }

    @Test
    fun launch_thenPassesCorrectPeriod() = runTest {
        underTest.launch(TEST_TICKER, "month")

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

        val result = underTest.launch(TEST_TICKER, "week")

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

        val result = underTest.launch(TEST_TICKER, "week")

        assertTrue(result.isLeft())
        result.onLeft { error ->
            assertIs<DomainError.GeneralError>(error)
            assertEquals("Unexpected failure", error.exception.message)
        }
    }
}
