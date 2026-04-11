package com.test.app.list

import androidx.paging.testing.asSnapshot
import app.cash.turbine.test
import com.feature.list.impl.StocksListViewModel
import com.test.app.testing.data.testPagingData
import com.test.app.testing.data.testStockOverviewLists
import com.core.testing.repository.TestStocksRepository
import com.test.app.testing.utils.BaseCoroutineTestWithInstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class StockListViewModelTest : BaseCoroutineTestWithInstantTaskExecutorRule() {

    private lateinit var viewModel: StocksListViewModel

    private var testCodeChallengesRepository = TestStocksRepository()

    private var getCodeChallengesUseCase = GetCodeChallengesUseCase(testCodeChallengesRepository)

    @Before
    fun setUp() {
        viewModel = StocksListViewModel(getCodeChallengesUseCase = getCodeChallengesUseCase)
    }

    @Test
    fun `test codeChallenges`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.stocksPaging.asSnapshot()
        }

        assertEquals(testStockOverviewLists, viewModel.stocksPaging.asSnapshot())

        collectJob.cancel()
    }

    @Test
    fun `test flow`() = runTest {
        coEvery { repository.getStocksFlow() } returns flow {
            emit(testPagingData)
        }

        useCase.launch().test {
            val firstItem = awaitItem()
            firstItem.shouldBeEqualTo(testPagingData)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) {
            repository.getStocksFlow()
        }
    }
}
