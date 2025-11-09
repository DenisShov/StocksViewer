@file:OptIn(ExperimentalMaterial3Api::class)

package com.test.app.list

import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasScrollToNodeAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performScrollToNode
import androidx.paging.compose.collectAsLazyPagingItems
import com.test.app.testing.data.testStockOverviewLists
import com.test.app.testing.data.testErrorMessage
import com.test.app.testing.data.testFlowPagingData
import com.test.app.testing.data.testFlowPagingDataAppendError
import com.test.app.testing.data.testFlowPagingDataAppendLoading
import com.test.app.testing.data.testFlowPagingDataNotLoading
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CodeChallengesScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var retry: String
    private lateinit var appName: String

    @Before
    fun setup() {
        composeTestRule.activity.apply {
            retry = getString(com.test.app.commonresources.R.string.retry)
            appName = getString(com.test.app.commonresources.R.string.app_name)
        }
    }

    @Test
    fun test_CodeChallengesScreen_title_is_displayed() {
        composeTestRule.setContent {
            val codeChallenges = testFlowPagingData.collectAsLazyPagingItems()
            val snackBarHostState = remember { SnackbarHostState() }
            val pullToRefreshState = rememberPullToRefreshState()

            StocksListScreen(
                stocksPaging = codeChallenges,
                snackBarHostState = snackBarHostState,
                pullToRefreshState = pullToRefreshState,
            )
        }

        composeTestRule
            .onNodeWithText(appName)
            .assertIsDisplayed()
    }

    @Test
    fun test_CodeChallengesScreen_displays_loading() {
        composeTestRule.setContent {
            val codeChallenges = testFlowPagingData.collectAsLazyPagingItems()
            val snackBarHostState = remember { SnackbarHostState() }
            val pullToRefreshState = rememberPullToRefreshState()

            StocksListScreen(
                stocksPaging = codeChallenges,
                snackBarHostState = snackBarHostState,
                pullToRefreshState = pullToRefreshState,
            )
        }

        composeTestRule
            .onNodeWithTag("CircularProgressIndicator")
            .assertIsDisplayed()
    }

    @Test
    fun test_CodeChallengesScreen_displays_items() {
        composeTestRule.setContent {
            val codeChallenges = testFlowPagingDataNotLoading.collectAsLazyPagingItems()
            val snackBarHostState = remember { SnackbarHostState() }
            val pullToRefreshState = rememberPullToRefreshState()

            StocksListScreen(
                stocksPaging = codeChallenges,
                snackBarHostState = snackBarHostState,
                pullToRefreshState = pullToRefreshState,
            )
        }

        val scrollableNode = composeTestRule
            .onAllNodes(hasScrollToNodeAction())
            .onFirst()

        testStockOverviewLists.forEachIndexed { index, codeChallenge ->
            scrollableNode.performScrollToIndex(index)

            composeTestRule
                .onNodeWithText(codeChallenge.name)
                .assertIsDisplayed()
        }
    }

    @Test
    fun test_CodeChallengesScreen_displays_append_loading() {
        composeTestRule.setContent {
            val codeChallenges = testFlowPagingDataAppendLoading.collectAsLazyPagingItems()
            val snackBarHostState = remember { SnackbarHostState() }
            val pullToRefreshState = rememberPullToRefreshState()

            StocksListScreen(
                stocksPaging = codeChallenges,
                snackBarHostState = snackBarHostState,
                pullToRefreshState = pullToRefreshState,
            )
        }

        composeTestRule
            .onAllNodes(hasScrollToNodeAction())
            .onFirst()
            .performScrollToNode(hasTestTag("AppendLoading"))
    }

    @Test
    fun test_CodeChallengesScreen_displays_append_error() {
        composeTestRule.setContent {
            val codeChallenges = testFlowPagingDataAppendError.collectAsLazyPagingItems()
            val snackBarHostState = remember { SnackbarHostState() }
            val pullToRefreshState = rememberPullToRefreshState()

            StocksListScreen(
                stocksPaging = codeChallenges,
                snackBarHostState = snackBarHostState,
                pullToRefreshState = pullToRefreshState,
            )
        }

        composeTestRule
            .onAllNodes(hasScrollToNodeAction())
            .onFirst()
            .performScrollToNode(hasText(testErrorMessage))
            .performScrollToNode(hasText(retry))
    }

}
