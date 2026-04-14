package com.feature.list.impl

import androidx.activity.ComponentActivity
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasScrollToNodeAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performScrollToNode
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.feature.list.impl.compose.StocksListScreen
import com.feature.list.impl.model.TickerUiModel
import com.feature.list.impl.paging.SearchResultsError
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class StockListScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun whenLoading_thenShowsSkeleton() {
        val flow = MutableStateFlow(
            PagingData.from<TickerUiModel>(
                data = emptyList(),
                sourceLoadStates = LoadStates(
                    refresh = LoadState.Loading,
                    append = LoadState.NotLoading(false),
                    prepend = LoadState.NotLoading(false),
                ),
            )
        )

        composeTestRule.setContent {
            val items = flow.collectAsLazyPagingItems()
            val pullToRefreshState = rememberPullToRefreshState()
            StocksListScreen(
                stocksPaging = items,
                pullToRefreshState = pullToRefreshState,
                isRefreshing = false,
            )
        }

        composeTestRule
            .onNodeWithTag("stocks_list_loading_skeleton")
            .assertIsDisplayed()
    }

    @Test
    fun whenLoading_thenListIsNotDisplayed() {
        val flow = MutableStateFlow(
            PagingData.from<TickerUiModel>(
                data = emptyList(),
                sourceLoadStates = LoadStates(
                    refresh = LoadState.Loading,
                    append = LoadState.NotLoading(false),
                    prepend = LoadState.NotLoading(false),
                ),
            )
        )

        composeTestRule.setContent {
            val items = flow.collectAsLazyPagingItems()
            val pullToRefreshState = rememberPullToRefreshState()
            StocksListScreen(
                stocksPaging = items,
                pullToRefreshState = pullToRefreshState,
                isRefreshing = false,
            )
        }

        composeTestRule
            .onNodeWithTag("stocks_list")
            .assertDoesNotExist()
    }

    @Test
    fun whenContentLoaded_thenShowsList() {
        val flow = MutableStateFlow(
            PagingData.from(testItems, sourceLoadStates = notLoadingStates())
        )

        composeTestRule.setContent {
            val items = flow.collectAsLazyPagingItems()
            val pullToRefreshState = rememberPullToRefreshState()
            StocksListScreen(
                stocksPaging = items,
                pullToRefreshState = pullToRefreshState,
                isRefreshing = false,
            )
        }

        composeTestRule
            .onNodeWithTag("stocks_list")
            .assertIsDisplayed()
    }

    @Test
    fun whenContentLoaded_thenDisplaysStockItems() {
        val flow = MutableStateFlow(
            PagingData.from(testItems, sourceLoadStates = notLoadingStates())
        )

        composeTestRule.setContent {
            val items = flow.collectAsLazyPagingItems()
            val pullToRefreshState = rememberPullToRefreshState()
            StocksListScreen(
                stocksPaging = items,
                pullToRefreshState = pullToRefreshState,
                isRefreshing = false,
            )
        }

        composeTestRule
            .onNodeWithText("Apple Inc.")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Alphabet Inc.")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Microsoft Corporation")
            .assertIsDisplayed()
    }

    @Test
    fun whenContentLoaded_thenDisplaysStockType() {
        val flow = MutableStateFlow(
            PagingData.from(testItems, sourceLoadStates = notLoadingStates())
        )

        composeTestRule.setContent {
            val items = flow.collectAsLazyPagingItems()
            val pullToRefreshState = rememberPullToRefreshState()
            StocksListScreen(
                stocksPaging = items,
                pullToRefreshState = pullToRefreshState,
                isRefreshing = false,
            )
        }

        composeTestRule
            .onNodeWithTag("stock_list_item_AAPL")
            .assertIsDisplayed()
    }

    @Test
    fun whenStockItemClicked_thenCallsOnStockClick() {
        var clickedTicker: String? = null
        val flow = MutableStateFlow(
            PagingData.from(testItems, sourceLoadStates = notLoadingStates())
        )

        composeTestRule.setContent {
            val items = flow.collectAsLazyPagingItems()
            val pullToRefreshState = rememberPullToRefreshState()
            StocksListScreen(
                stocksPaging = items,
                pullToRefreshState = pullToRefreshState,
                isRefreshing = false,
                onStockClick = { clickedTicker = it },
            )
        }

        composeTestRule
            .onNodeWithTag("stock_list_item_AAPL")
            .performClick()

        assert(clickedTicker == "AAPL")
    }

    @Test
    fun whenManyItems_thenCanScrollToAll() {
        val manyItems = (1..20).map {
            TickerUiModel("T$it", "Company $it", "Common Stock")
        }
        val flow = MutableStateFlow(
            PagingData.from(manyItems, sourceLoadStates = notLoadingStates())
        )

        composeTestRule.setContent {
            val items = flow.collectAsLazyPagingItems()
            val pullToRefreshState = rememberPullToRefreshState()
            StocksListScreen(
                stocksPaging = items,
                pullToRefreshState = pullToRefreshState,
                isRefreshing = false,
            )
        }

        val scrollableNode = composeTestRule
            .onAllNodes(hasScrollToNodeAction())
            .onFirst()

        scrollableNode.performScrollToIndex(19)

        composeTestRule
            .onNodeWithText("Company 20")
            .assertIsDisplayed()
    }

    @Test
    fun whenRefreshError_thenShowsError() {
        val flow = MutableStateFlow(
            PagingData.from<TickerUiModel>(
                data = emptyList(),
                sourceLoadStates = LoadStates(
                    refresh = LoadState.Error(SearchResultsError.NetworkError),
                    append = LoadState.NotLoading(false),
                    prepend = LoadState.NotLoading(false),
                ),
            )
        )

        composeTestRule.setContent {
            val items = flow.collectAsLazyPagingItems()
            val pullToRefreshState = rememberPullToRefreshState()
            StocksListScreen(
                stocksPaging = items,
                pullToRefreshState = pullToRefreshState,
                isRefreshing = false,
            )
        }

        composeTestRule
            .onNodeWithTag("stocks_list_error")
            .assertIsDisplayed()
    }

    @Test
    fun whenAppendLoading_thenShowsAppendSpinner() {
        val flow = MutableStateFlow(
            PagingData.from(
                data = testItems,
                sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(false),
                    append = LoadState.Loading,
                    prepend = LoadState.NotLoading(false),
                ),
            )
        )

        composeTestRule.setContent {
            val items = flow.collectAsLazyPagingItems()
            val pullToRefreshState = rememberPullToRefreshState()
            StocksListScreen(
                stocksPaging = items,
                pullToRefreshState = pullToRefreshState,
                isRefreshing = false,
            )
        }

        composeTestRule
            .onAllNodes(hasScrollToNodeAction())
            .onFirst()
            .performScrollToNode(hasTestTag("stocks_list_append_loading"))

        composeTestRule
            .onNodeWithTag("stocks_list_append_loading")
            .assertIsDisplayed()
    }

    private val testItems = listOf(
        TickerUiModel("AAPL", "Apple Inc.", "Common Stock"),
        TickerUiModel("GOOGL", "Alphabet Inc.", "Common Stock"),
        TickerUiModel("MSFT", "Microsoft Corporation", "Common Stock"),
    )

    private fun notLoadingStates() = LoadStates(
        refresh = LoadState.NotLoading(false),
        append = LoadState.NotLoading(false),
        prepend = LoadState.NotLoading(false),
    )
}
