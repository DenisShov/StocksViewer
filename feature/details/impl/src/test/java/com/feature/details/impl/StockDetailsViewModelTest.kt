package com.feature.details.impl

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import com.core.common.error.DomainError
import com.core.data.repository.StocksRepository
import com.core.domain.GetStockChartDataUseCase
import com.core.testing.data.TEST_TICKER
import com.core.testing.data.testStockChart
import com.core.testing.data.testStockDetails
import com.core.testing.utils.CoroutineTestRule
import com.feature.details.impl.actions.ChartPeriod
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class StockDetailsViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule(UnconfinedTestDispatcher())

    private val stocksRepository: StocksRepository = mockk()
    private val getStockChartDataUseCase: GetStockChartDataUseCase = mockk()
    private lateinit var sut: StockDetailsViewModel

    private fun createSut() = StockDetailsViewModel(
        ticker = TEST_TICKER,
        stocksRepository = stocksRepository,
        getStockChartDataUseCase = getStockChartDataUseCase,
    )

    @Before
    fun setup() {
        coEvery { stocksRepository.getStockOverviewByTicker(any()) } returns testStockDetails.right()
        coEvery { getStockChartDataUseCase.launch(any(), any()) } returns testStockChart.right()
        sut = createSut()
    }

    @Test
    fun whenOverviewLoads_thenShowsStockOverview() = runTest {
        sut.uiState.test {
            val state = awaitItem()
            assertNotNull(state.stockOverview)
            assertEquals(TEST_TICKER, state.stockOverview.ticker)
            assertEquals(false, state.isLoading)
            assertNull(state.errorString)
        }
    }

    @Test
    fun whenOverviewLoads_thenAlsoLoadsChartData() = runTest {
        sut.uiState.test {
            val state = awaitItem()
            assertTrue(state.candles.isNotEmpty())
            assertEquals(false, state.isLoading)
        }
    }

    @Test
    fun whenOverviewFails_thenShowsError() = runTest {
        coEvery {
            stocksRepository.getStockOverviewByTicker(any())
        } returns DomainError.MissingNetworkConnection.left()

        sut.uiState.test {
            val state = awaitItem()
            assertNull(state.stockOverview)
            assertIs<DomainError.MissingNetworkConnection>(state.errorString)
            assertEquals(false, state.isLoading)
        }
    }

    @Test
    fun whenChartPeriodChanges_thenUpdatesCandles() = runTest {
        sut.uiState.test {
            expectMostRecentItem()

            sut.getStockChartData(ChartPeriod.MONTH)

            val state = expectMostRecentItem()
            assertTrue(state.candles.isNotEmpty())
            assertEquals(ChartPeriod.MONTH, state.selectedPeriod)
            assertEquals(false, state.isLoading)
        }
    }

    @Test
    fun whenChartDataFails_thenShowsError() = runTest {
        coEvery {
            getStockChartDataUseCase.launch(any(), any())
        } returns DomainError.HttpError(500, "Internal Server Error").left()

        sut.uiState.test {
            val state = awaitItem()
            assertNotNull(state.stockOverview)
            assertIs<DomainError.HttpError>(state.errorString)
            assertEquals(500, state.errorString.code)
        }
    }

    @Test
    fun whenRetry_thenReloadsData() = runTest {
        sut.uiState.test {
            val initialState = expectMostRecentItem()
            assertNotNull(initialState.stockOverview)

            sut.getStockOverviewByTicker()

            val state = expectMostRecentItem()
            assertNotNull(state.stockOverview)
            assertEquals(false, state.isLoading)
        }
    }
}
