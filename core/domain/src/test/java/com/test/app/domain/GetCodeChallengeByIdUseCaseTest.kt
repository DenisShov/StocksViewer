package com.test.app.domain

import app.cash.turbine.test
import com.test.app.common.error.AppError
import com.test.app.common.result.DataResult
import com.test.app.data.repository.CodeChallengesRepositoryImpl
import com.test.app.testing.data.testCodeChallengeDetail
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
    private lateinit var useCase: GetCodeChallengeByIdUseCase

    @MockK
    private lateinit var repository: CodeChallengesRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        useCase = GetCodeChallengeByIdUseCase(
            dispatcherProvider = testDispatcherProvider,
            codeChallengesRepository = repository
        )
    }

    @Test
    fun `test use case`() = runTest {
        val challengeId = "id"

        coEvery { repository.getCodeChallengeById(challengeId) } returns testCodeChallengeDetail

        useCase.launch(challengeId).test {
            val firstItem = awaitItem()
            firstItem.shouldBeEqualTo(DataResult.Loading)

            val secondItem = awaitItem()
            secondItem.shouldBeEqualTo(DataResult.Success(testCodeChallengeDetail))
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) {
            repository.getCodeChallengeById(challengeId)
        }
    }

    @Test
    fun `test use case handles errors`() = runTest {
        val challengeId = "id"

        val exception = mockk<IllegalStateException>()
        coEvery { repository.getCodeChallengeById(challengeId) } throws exception

        useCase.launch(challengeId).test {
            val firstItem = awaitItem()
            firstItem.shouldBeEqualTo(DataResult.Loading)

            val secondItem = awaitItem()
            secondItem.shouldBeEqualTo(DataResult.Failure(AppError.GeneralError(exception)))
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) {
            repository.getCodeChallengeById(challengeId)
        }
    }
}
