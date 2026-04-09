package com.test.app.details

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.test.app.common.error.DomainError
import com.test.app.common.result.DataResult
import com.test.app.impl.StockDetailsViewModel
import com.test.app.impl.StockDetailsViewModel.Companion.STOCK_TICKER_ARG
import com.test.app.domain.GetStockOverviewByTickerUseCase
import com.test.app.testing.data.testStockDetails
import com.test.app.testing.utils.BaseCoroutineTestWithInstantTaskExecutorRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class StockDetailViewModelTest : BaseCoroutineTestWithInstantTaskExecutorRule() {

    private lateinit var viewModel: StockDetailsViewModel

    @MockK
    private lateinit var getStockOverviewByTickerUseCase: GetStockOverviewByTickerUseCase

    private val savedStateHandle =
        SavedStateHandle(mapOf(STOCK_TICKER_ARG to testStockDetails.id))

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        viewModel = StockDetailsViewModel(
            savedStateHandle = savedStateHandle,
            getStockOverviewByTickerUseCase = getStockOverviewByTickerUseCase,
        )
    }

    @Test
    fun codeChallengeId_matchesCodeChallengeIdFromSavedStateHandle() =
        assertEquals(testStockDetails.id, viewModel.ticker)

    @Test
    fun `test getCodeChallengeById`() = runTest {
        coEvery { getStockOverviewByTickerUseCase.launch(viewModel.ticker) } returns flow {
            emit(DataResult.Success(testStockDetails))
        }

        viewModel.getStockOverviewByTicker()

        viewModel.uiState.test {
            awaitItem().shouldBeEqualTo(
                StockDetailsViewModel.State(
                    StockDetailsViewModel.StockDetailsState.Loading
                )
            )
            awaitItem().shouldBeEqualTo(
                StockDetailsViewModel.State(
                    StockDetailsViewModel.StockDetailsState.Success(
                        testStockDetails
                    )
                )
            )
            cancelAndConsumeRemainingEvents()
        }

        coVerify(exactly = 1) {
            getStockOverviewByTickerUseCase.launch(viewModel.ticker)
        }
    }

    @Test
    fun `test getCodeChallengeById handles error`() = runTest {

        val exception = mockk<IllegalStateException>()
        coEvery { getStockOverviewByTickerUseCase.launch(viewModel.ticker) } returns
                flow {
                    emit(DataResult.Failure(DomainError.GeneralError(exception)))
                }

        viewModel.getStockOverviewByTicker()

        viewModel.uiState.test {
            awaitItem().shouldBeEqualTo(
                StockDetailsViewModel.State(
                    StockDetailsViewModel.StockDetailsState.Loading
                )
            )
            awaitItem().shouldBeEqualTo(
                StockDetailsViewModel.State(
                    StockDetailsViewModel.StockDetailsState.Error(
                        DomainError.GeneralError(exception)
                    )
                )
            )

            cancelAndConsumeRemainingEvents()
        }
        coVerify(exactly = 1) {
            getStockOverviewByTickerUseCase.launch(viewModel.ticker)
        }
    }
}
