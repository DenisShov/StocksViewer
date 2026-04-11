package com.core.domain

import app.cash.turbine.test
import com.core.testing.data.testStockDetails
import com.core.testing.utils.BaseCoroutineTestWithTestDispatcherProvider
import com.core.common.error.DomainError
import com.test.app.common.result.DataResult
import com.core.data.repository.StocksRepositoryImpl
import com.test.app.testing.data.testStockDetails
import com.test.app.testing.utils.BaseCoroutineTestWithTestDispatcherProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test

class GetCodeChallengeByIdUseCaseTest : BaseCoroutineTestWithTestDispatcherProvider() {
    private lateinit var useCase: GetStockOverviewByTickerUseCase

    @MockK
    private lateinit var repository: StocksRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        useCase = GetStockOverviewByTickerUseCase(
            dispatcherProvider = testDispatcherProvider,
            stocksRepository = repository
        )
    }

    @Test
    fun `test use case`() = runTest {
        val challengeId = "id"

        coEvery { repository.getStockOverviewByTicker(challengeId) } returns testStockDetails

        useCase.launch(challengeId).test {
            val firstItem = awaitItem()
            firstItem.shouldBeEqualTo(DataResult.Loading)

            val secondItem = awaitItem()
            secondItem.shouldBeEqualTo(DataResult.Success(testStockDetails))
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) {
            repository.getStockOverviewByTicker(challengeId)
        }
    }

    @Test
    fun `test use case handles errors`() = runTest {
        val challengeId = "id"

        val exception = mockk<IllegalStateException>()
        coEvery { repository.getStockOverviewByTicker(challengeId) } throws exception

        useCase.launch(challengeId).test {
            val firstItem = awaitItem()
            firstItem.shouldBeEqualTo(DataResult.Loading)

            val secondItem = awaitItem()
            secondItem.shouldBeEqualTo(DataResult.Failure(DomainError.GeneralError(exception)))
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) {
            repository.getStockOverviewByTicker(challengeId)
        }
    }
}
