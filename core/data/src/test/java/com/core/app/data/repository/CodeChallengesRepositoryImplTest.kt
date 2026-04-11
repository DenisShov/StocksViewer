package com.core.app.data.repository

import androidx.paging.Pager
import com.test.app.model.data.StockOverview
import com.test.app.network.retrofit.StocksApi
import com.test.app.testing.data.testStockDetails
import com.test.app.testing.data.testFlowPagingData
import com.test.app.testing.data.testNetworkCodeChallengeDetail
import com.test.app.testing.utils.BaseCoroutineTestWithTestDispatcherProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test

class CodeChallengesRepositoryImplTest : BaseCoroutineTestWithTestDispatcherProvider() {

    private lateinit var repository: StocksRepository

    @MockK
    private lateinit var getStocksApi: StocksApi

    @MockK
    private lateinit var pager: Pager<Int, StockOverview>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        repository = StocksRepositoryImpl(stocksApi = getStocksApi, pager = pager)
    }

    @Test
    fun `test getCodeChallengesFlow`() = runTest {
        coEvery { pager.flow } returns testFlowPagingData

        val flow = repository.getStocksFlow()

        flow.shouldBeEqualTo(testFlowPagingData)

        coVerify(exactly = 1) {
            pager.flow
        }
    }

    @Test
    fun `test getCodeChallengeById`() = runTest {
        val codeChallengeId = "id"

        coEvery { getStocksApi.getCodeChallenge(codeChallengeId) } returns testNetworkCodeChallengeDetail

        val coinMarkets = repository.getStockOverviewByTicker(codeChallengeId)

        coinMarkets.shouldBeEqualTo(testStockDetails)

        coVerify(exactly = 1) {
            getStocksApi.getCodeChallenge(codeChallengeId)
        }
    }

}
