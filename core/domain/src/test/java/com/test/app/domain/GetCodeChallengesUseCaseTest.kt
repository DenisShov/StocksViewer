package com.test.app.domain

import app.cash.turbine.test
import com.test.app.data.repository.StocksRepository
import com.test.app.testing.data.testPagingData
import com.test.app.testing.utils.BaseCoroutineTestWithTestDispatcherProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test

class GetCodeChallengesUseCaseTest : BaseCoroutineTestWithTestDispatcherProvider() {

    private lateinit var useCase: GetCodeChallengesUseCase

    @MockK
    private lateinit var repository: StocksRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        useCase = GetCodeChallengesUseCase(stocksRepository = repository)
    }

    @Test
    fun `test use case`() = runTest {
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
