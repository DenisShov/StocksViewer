package com.test.app.details

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.test.app.common.error.AppError
import com.test.app.common.result.DataResult
import com.test.app.details.CodeChallengeDetailViewModel.Companion.CODE_CHALLENGE_ID_ARG
import com.test.app.domain.GetCodeChallengeByIdUseCase
import com.test.app.testing.data.testCodeChallengeDetail
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

class CodeChallengeDetailViewModelTest : BaseCoroutineTestWithInstantTaskExecutorRule() {

    private lateinit var viewModel: CodeChallengeDetailViewModel

    @MockK
    private lateinit var getCodeChallengeByIdUseCase: GetCodeChallengeByIdUseCase

    private val savedStateHandle =
        SavedStateHandle(mapOf(CODE_CHALLENGE_ID_ARG to testCodeChallengeDetail.id))

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        viewModel = CodeChallengeDetailViewModel(
            savedStateHandle = savedStateHandle,
            getCodeChallengeByIdUseCase = getCodeChallengeByIdUseCase,
        )
    }

    @Test
    fun codeChallengeId_matchesCodeChallengeIdFromSavedStateHandle() =
        assertEquals(testCodeChallengeDetail.id, viewModel.codeChallengeId)

    @Test
    fun `test getCodeChallengeById`() = runTest {
        coEvery { getCodeChallengeByIdUseCase.launch(viewModel.codeChallengeId) } returns flow {
            emit(DataResult.Success(testCodeChallengeDetail))
        }

        viewModel.getCodeChallengeById()

        viewModel.uiState.test {
            awaitItem().shouldBeEqualTo(
                CodeChallengeDetailViewModel.State(
                    CodeChallengeDetailViewModel.CodeChallengeState.Loading
                )
            )
            awaitItem().shouldBeEqualTo(
                CodeChallengeDetailViewModel.State(
                    CodeChallengeDetailViewModel.CodeChallengeState.Success(
                        testCodeChallengeDetail
                    )
                )
            )
            cancelAndConsumeRemainingEvents()
        }

        coVerify(exactly = 1) {
            getCodeChallengeByIdUseCase.launch(viewModel.codeChallengeId)
        }
    }

    @Test
    fun `test getCodeChallengeById handles error`() = runTest {

        val exception = mockk<IllegalStateException>()
        coEvery { getCodeChallengeByIdUseCase.launch(viewModel.codeChallengeId) } returns
                flow {
                    emit(DataResult.Failure(AppError.GeneralError(exception)))
                }

        viewModel.getCodeChallengeById()

        viewModel.uiState.test {
            awaitItem().shouldBeEqualTo(
                CodeChallengeDetailViewModel.State(
                    CodeChallengeDetailViewModel.CodeChallengeState.Loading
                )
            )
            awaitItem().shouldBeEqualTo(
                CodeChallengeDetailViewModel.State(
                    CodeChallengeDetailViewModel.CodeChallengeState.Error(
                        AppError.GeneralError(exception)
                    )
                )
            )

            cancelAndConsumeRemainingEvents()
        }
        coVerify(exactly = 1) {
            getCodeChallengeByIdUseCase.launch(viewModel.codeChallengeId)
        }
    }
}
