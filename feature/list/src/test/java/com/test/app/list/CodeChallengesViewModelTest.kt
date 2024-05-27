package com.test.app.list

import androidx.paging.testing.asSnapshot
import com.test.app.domain.GetCodeChallengesUseCase
import com.test.app.testing.data.testCodeChallengeOverviewList
import com.test.app.testing.repository.TestCodeChallengesRepository
import com.test.app.testing.utils.BaseCoroutineTestWithInstantTaskExecutorRule
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test

class CodeChallengesViewModelTest : BaseCoroutineTestWithInstantTaskExecutorRule() {

    private lateinit var viewModel: CodeChallengesViewModel

    private var testCodeChallengesRepository = TestCodeChallengesRepository()

    private var getCodeChallengesUseCase = GetCodeChallengesUseCase(testCodeChallengesRepository)

    @Before
    fun setUp() {
        viewModel = CodeChallengesViewModel(
            getCodeChallengesUseCase = getCodeChallengesUseCase,
        )
    }

    @Test
    fun `test codeChallenges`() = runTest {
        val collectJob =
            launch(UnconfinedTestDispatcher()) {
                viewModel.codeChallenges.asSnapshot().shouldBeEqualTo(testCodeChallengeOverviewList)
            }

        collectJob.cancel()
    }
}
