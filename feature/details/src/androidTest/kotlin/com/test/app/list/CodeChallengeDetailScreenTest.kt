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
import com.test.app.common.error.DomainError
import com.test.app.details.StockDetailsScreen
import com.test.app.details.StockDetailsViewModel
import com.test.app.testing.data.testStockDetails
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
            StockDetailsScreen(
                snackBarHostState = snackBarHostState,

                uiState = StockDetailsViewModel.State(
                    stockDetailsState = StockDetailsViewModel.StockDetailsState.Loading
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
            StockDetailsScreen(
                snackBarHostState = snackBarHostState,

                uiState = StockDetailsViewModel.State(
                    stockDetailsState = StockDetailsViewModel.StockDetailsState.Loading
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
            StockDetailsScreen(
                snackBarHostState = snackBarHostState,

                uiState = StockDetailsViewModel.State(
                    stockDetailsState = StockDetailsViewModel.StockDetailsState.Success(
                        testStockDetails
                    )
                )
            )
        }

        scrollToTexts(
            listOf(
                testStockDetails.name!!,
                categoryTitle,
                testStockDetails.category!!,
                descriptionTitle,
                testStockDetails.description!!,
                rankTitle,
                testStockDetails.rank!!.name!!,
                createdByTitle,
                testStockDetails.createdBy!!.username!!,
                approvedByTitle,
                testStockDetails.approvedBy!!.username!!,
                totalAttemptsTitle,
                testStockDetails.totalAttempts.toStr(),
                totalCompletedTitle,
                testStockDetails.totalCompleted.toStr(),
                totalStarsTitle,
                testStockDetails.totalStars.toStr(),
                voteScoreTitle,
                testStockDetails.voteScore.toStr(),
                publishedAtTitle,
                testStockDetails.publishedAt.toStr(),
                approvedAtTitle,
                testStockDetails.approvedAt.toStr(),
                tagsTitle,
                languagesTitle
            )
        )
        scrollToTexts(testStockDetails.tags!!)
        scrollToTexts(testStockDetails.languages!!)
    }

    @Test
    fun check_Error_is_displayed() {
        composeTestRule.setContent {
            val snackBarHostState = remember { SnackbarHostState() }
            StockDetailsScreen(
                snackBarHostState = snackBarHostState,

                uiState = StockDetailsViewModel.State(
                    stockDetailsState = StockDetailsViewModel.StockDetailsState.Error(
                        error = DomainError.GeneralError(RuntimeException("Some error"))
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
