package com.test.app.data.repository

import androidx.paging.Pager
import com.test.app.data.repository.CodeChallengesRepository
import com.test.app.data.repository.CodeChallengesRepositoryImpl
import com.test.app.model.data.CodeChallengeOverview
import com.test.app.network.retrofit.CodeWarsApi
import com.test.app.testing.data.testCodeChallengeDetail
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

    private lateinit var repository: CodeChallengesRepository

    @MockK
    private lateinit var getCodeWarsApi: CodeWarsApi

    @MockK
    private lateinit var pager: Pager<Int, CodeChallengeOverview>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        repository = CodeChallengesRepositoryImpl(codeWarsApi = getCodeWarsApi, pager = pager)
    }

    @Test
    fun `test getCodeChallengesFlow`() = runTest {
        coEvery { pager.flow } returns testFlowPagingData

        val flow = repository.getCodeChallengesFlow()

        flow.shouldBeEqualTo(testFlowPagingData)

        coVerify(exactly = 1) {
            pager.flow
        }
    }

    @Test
    fun `test getCodeChallengeById`() = runTest {
        val codeChallengeId = "id"

        coEvery { getCodeWarsApi.getCodeChallenge(codeChallengeId) } returns testNetworkCodeChallengeDetail

        val coinMarkets = repository.getCodeChallengeById(codeChallengeId)

        coinMarkets.shouldBeEqualTo(testCodeChallengeDetail)

        coVerify(exactly = 1) {
            getCodeWarsApi.getCodeChallenge(codeChallengeId)
        }
    }

}
