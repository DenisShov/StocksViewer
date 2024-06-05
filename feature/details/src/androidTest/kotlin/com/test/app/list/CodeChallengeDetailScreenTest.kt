package com.test.app.list

import androidx.activity.ComponentActivity
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasScrollToNodeAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToNode
import com.test.app.common.error.AppError
import com.test.app.details.CodeChallengeDetailScreen
import com.test.app.details.CodeChallengeDetailViewModel
import com.test.app.details.R
import com.test.app.testing.data.testCodeChallengeDetail
import io.mockk.InternalPlatformDsl.toStr
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CodeChallengeDetailScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var returnToPreviousScreen: String
    private lateinit var descriptionTitle: String
    private lateinit var categoryTitle: String
    private lateinit var rankTitle: String
    private lateinit var totalAttemptsTitle: String
    private lateinit var totalCompletedTitle: String
    private lateinit var totalStarsTitle: String
    private lateinit var voteScoreTitle: String
    private lateinit var createdByTitle: String
    private lateinit var approvedByTitle: String
    private lateinit var publishedAtTitle: String
    private lateinit var approvedAtTitle: String
    private lateinit var tagsTitle: String
    private lateinit var languagesTitle: String

    private lateinit var loadingDataText: String
    private lateinit var someErrorHappened: String

    @Before
    fun setup() {
        composeTestRule.activity.apply {
            returnToPreviousScreen =
                getString(com.test.app.commonresources.R.string.ui_return_to_previous_screen)
            descriptionTitle = getString(com.test.app.commonresources.R.string.description)
            categoryTitle = getString(com.test.app.commonresources.R.string.category)
            rankTitle = getString(com.test.app.commonresources.R.string.rank)
            totalAttemptsTitle = getString(com.test.app.commonresources.R.string.total_attempts)
            totalCompletedTitle = getString(com.test.app.commonresources.R.string.total_completed)
            totalStarsTitle = getString(com.test.app.commonresources.R.string.total_stars)
            voteScoreTitle = getString(com.test.app.commonresources.R.string.total_score)
            createdByTitle = getString(com.test.app.commonresources.R.string.created_by)
            approvedByTitle = getString(com.test.app.commonresources.R.string.approved_by)
            publishedAtTitle = getString(com.test.app.commonresources.R.string.published_at)
            approvedAtTitle = getString(com.test.app.commonresources.R.string.approved_at)
            tagsTitle = getString(com.test.app.commonresources.R.string.tags)
            languagesTitle = getString(com.test.app.commonresources.R.string.languages)
            loadingDataText = getString(com.test.app.commonresources.R.string.loading_data)
            someErrorHappened = getString(com.test.app.commonresources.R.string.some_error_happened)
        }
    }

    @Test
    fun check_top_bar_back_button_is_displayed() {
        composeTestRule.setContent {
            val snackBarHostState = remember { SnackbarHostState() }
            CodeChallengeDetailScreen(
                snackBarHostState = snackBarHostState,

                uiState = CodeChallengeDetailViewModel.State(
                    codeChallengeState = CodeChallengeDetailViewModel.CodeChallengeState.Loading
                )
            )
        }

        composeTestRule
            .onNodeWithContentDescription(returnToPreviousScreen)
            .assertIsDisplayed()
    }

    @Test
    fun check_loading_is_displayed() {
        composeTestRule.setContent {
            val snackBarHostState = remember { SnackbarHostState() }
            CodeChallengeDetailScreen(
                snackBarHostState = snackBarHostState,

                uiState = CodeChallengeDetailViewModel.State(
                    codeChallengeState = CodeChallengeDetailViewModel.CodeChallengeState.Loading
                )
            )
        }

        composeTestRule
            .onNodeWithText(loadingDataText)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("CircularProgressIndicator")
            .assertIsDisplayed()
    }

    @Test
    fun check_content_is_displayed() {
        composeTestRule.setContent {
            val snackBarHostState = remember { SnackbarHostState() }
            CodeChallengeDetailScreen(
                snackBarHostState = snackBarHostState,

                uiState = CodeChallengeDetailViewModel.State(
                    codeChallengeState = CodeChallengeDetailViewModel.CodeChallengeState.Success(
                        testCodeChallengeDetail
                    )
                )
            )
        }

        scrollToTexts(
            listOf(
                testCodeChallengeDetail.name!!,
                categoryTitle,
                testCodeChallengeDetail.category!!,
                descriptionTitle,
                testCodeChallengeDetail.description!!,
                rankTitle,
                testCodeChallengeDetail.rank!!.name!!,
                createdByTitle,
                testCodeChallengeDetail.createdBy!!.username!!,
                approvedByTitle,
                testCodeChallengeDetail.approvedBy!!.username!!,
                totalAttemptsTitle,
                testCodeChallengeDetail.totalAttempts.toStr(),
                totalCompletedTitle,
                testCodeChallengeDetail.totalCompleted.toStr(),
                totalStarsTitle,
                testCodeChallengeDetail.totalStars.toStr(),
                voteScoreTitle,
                testCodeChallengeDetail.voteScore.toStr(),
                publishedAtTitle,
                testCodeChallengeDetail.publishedAt.toStr(),
                approvedAtTitle,
                testCodeChallengeDetail.approvedAt.toStr(),
                tagsTitle,
                languagesTitle
            )
        )
        scrollToTexts(testCodeChallengeDetail.tags!!)
        scrollToTexts(testCodeChallengeDetail.languages!!)
    }

    @Test
    fun check_Error_is_displayed() {
        composeTestRule.setContent {
            val snackBarHostState = remember { SnackbarHostState() }
            CodeChallengeDetailScreen(
                snackBarHostState = snackBarHostState,

                uiState = CodeChallengeDetailViewModel.State(
                    codeChallengeState = CodeChallengeDetailViewModel.CodeChallengeState.Error(
                        error = AppError.GeneralError(RuntimeException("Some error"))
                    )
                )
            )
        }

        composeTestRule
            .onNodeWithText(someErrorHappened)
            .assertIsDisplayed()
    }

    private fun scrollToTexts(list: List<String>) {
        for (text in list) {
            composeTestRule
                .onAllNodes(hasScrollToNodeAction())
                .onFirst()
                .performScrollToNode(hasText(text))
        }
    }

}
