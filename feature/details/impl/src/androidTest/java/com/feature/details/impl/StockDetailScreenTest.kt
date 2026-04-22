package com.feature.details.impl

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import com.feature.details.impl.ui.actions.ChartPeriod
import com.feature.details.impl.ui.actions.StockDetailsActions
import com.feature.details.impl.ui.compose.StockDetailScreen
import com.feature.details.impl.ui.model.CandleUiModel
import com.feature.details.impl.ui.model.StockOverviewUiModel
import com.feature.details.impl.ui.state.StockDetailsState
import org.junit.Rule
import org.junit.Test

class StockDetailScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun whenLoading_thenShowsSkeleton() {
        composeTestRule.setContent {
            StockDetailScreen(
                uiState = StockDetailsState(isLoading = true),
                actions = StockDetailsActions(),
            )
        }

        composeTestRule
            .onNodeWithTag("stock_details_loading_skeleton")
            .assertIsDisplayed()
    }

    @Test
    fun whenLoading_thenContentIsNotDisplayed() {
        composeTestRule.setContent {
            StockDetailScreen(
                uiState = StockDetailsState(isLoading = true),
                actions = StockDetailsActions(),
            )
        }

        composeTestRule
            .onNodeWithTag("stock_details_content")
            .assertDoesNotExist()
    }

    @Test
    fun whenError_thenShowsErrorMessage() {
        composeTestRule.setContent {
            StockDetailScreen(
                uiState = StockDetailsState(errorString = "No network connection"),
                actions = StockDetailsActions(),
            )
        }

        composeTestRule
            .onNodeWithTag("stock_details_error")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("No network connection")
            .assertIsDisplayed()
    }

    @Test
    fun whenError_thenRetryButtonCallsRetry() {
        var retryCalled = false

        composeTestRule.setContent {
            StockDetailScreen(
                uiState = StockDetailsState(errorString = "Something went wrong"),
                actions = StockDetailsActions(retry = { retryCalled = true }),
            )
        }

        val retryText = composeTestRule.activity.getString(com.core.commonresources.R.string.retry)
        composeTestRule
            .onNodeWithText(retryText)
            .performClick()

        assert(retryCalled)
    }

    @Test
    fun whenContent_thenShowsCompanyHeader() {
        composeTestRule.setContent {
            StockDetailScreen(
                uiState = StockDetailsState(
                    stockOverview = testOverview,
                    candles = testCandles,
                ),
                actions = StockDetailsActions(),
            )
        }

        composeTestRule
            .onNodeWithTag("company_header")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Apple Inc.")
            .assertIsDisplayed()
    }

    @Test
    fun whenContent_thenShowsTickerInTopBar() {
        composeTestRule.setContent {
            StockDetailScreen(
                uiState = StockDetailsState(
                    stockOverview = testOverview,
                    candles = testCandles,
                ),
                actions = StockDetailsActions(),
            )
        }

        composeTestRule
            .onNodeWithTag("stock_details_ticker_name")
            .assertIsDisplayed()
    }

    @Test
    fun whenContent_thenShowsKeyStats() {
        composeTestRule.setContent {
            StockDetailScreen(
                uiState = StockDetailsState(
                    stockOverview = testOverview,
                    candles = testCandles,
                ),
                actions = StockDetailsActions(),
            )
        }

        composeTestRule
            .onNodeWithTag("key_stats_grid")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("3.00T")
            .assertIsDisplayed()
    }

    @Test
    fun whenContentHasDescription_thenShowsAboutSection() {
        composeTestRule.setContent {
            StockDetailScreen(
                uiState = StockDetailsState(
                    stockOverview = testOverview,
                    candles = testCandles,
                ),
                actions = StockDetailsActions(),
            )
        }

        composeTestRule
            .onNodeWithTag("stock_details_content")
            .performScrollToNode(hasTestTag("about_section"))

        composeTestRule
            .onNodeWithTag("about_section")
            .assertIsDisplayed()
    }

    @Test
    fun whenContentHasNoDescription_thenAboutSectionIsHidden() {
        composeTestRule.setContent {
            StockDetailScreen(
                uiState = StockDetailsState(
                    stockOverview = testOverview.copy(description = null),
                    candles = testCandles,
                ),
                actions = StockDetailsActions(),
            )
        }

        composeTestRule
            .onNodeWithTag("about_section")
            .assertDoesNotExist()
    }

    @Test
    fun whenContent_thenShowsContactInfo() {
        composeTestRule.setContent {
            StockDetailScreen(
                uiState = StockDetailsState(
                    stockOverview = testOverview,
                    candles = testCandles,
                ),
                actions = StockDetailsActions(),
            )
        }

        composeTestRule
            .onNodeWithTag("stock_details_content")
            .performScrollToNode(hasTestTag("contact_info"))

        composeTestRule
            .onNodeWithTag("contact_info")
            .assertIsDisplayed()
    }

    @Test
    fun whenContent_thenShowsPeriodButtons() {
        composeTestRule.setContent {
            StockDetailScreen(
                uiState = StockDetailsState(
                    stockOverview = testOverview,
                    candles = testCandles,
                ),
                actions = StockDetailsActions(),
            )
        }

        composeTestRule
            .onNodeWithTag("stock_details_content")
            .performScrollToNode(hasTestTag("period_buttons"))

        composeTestRule
            .onNodeWithTag("period_buttons")
            .assertIsDisplayed()
    }

    @Test
    fun whenBackButtonClicked_thenCallsOnBackButtonClick() {
        var backClicked = false

        composeTestRule.setContent {
            StockDetailScreen(
                uiState = StockDetailsState(
                    stockOverview = testOverview,
                    candles = testCandles,
                ),
                onBackButtonClick = { backClicked = true },
                actions = StockDetailsActions(),
            )
        }

        composeTestRule
            .onNodeWithTag("stock_details_back_button")
            .performClick()

        assert(backClicked)
    }

    @Test
    fun whenPeriodButtonClicked_thenCallsOnChartPeriodChange() {
        var selectedPeriod: ChartPeriod? = null

        composeTestRule.setContent {
            StockDetailScreen(
                uiState = StockDetailsState(
                    stockOverview = testOverview,
                    candles = testCandles,
                ),
                actions = StockDetailsActions(
                    onChartPeriodChange = { selectedPeriod = it },
                ),
            )
        }

        composeTestRule
            .onNodeWithTag("stock_details_content")
            .performScrollToNode(hasTestTag("period_button_month"))

        composeTestRule
            .onNodeWithTag("period_button_month")
            .performClick()

        assert(selectedPeriod == ChartPeriod.MONTH)
    }

    private val testOverview = StockOverviewUiModel(
        ticker = "AAPL",
        name = "Apple Inc.",
        exchange = "XNAS",
        marketCap = "3.00T",
        totalEmployees = 164000,
        sicDescription = "Electronic Computers",
        description = "Apple Inc. designs, manufactures, and markets smartphones and personal computers.",
        address = "One Apple Park Way, Cupertino, CA",
        homepageUrl = "https://www.apple.com",
        listDate = "12 December 1980",
        cik = "0000320193",
    )

    private val testCandles = listOf(
        CandleUiModel(open = 185.82, close = 184.8, high = 186.03, low = 184.21, timestampMs = 1699851600000),
        CandleUiModel(open = 187.7, close = 187.44, high = 188.11, low = 186.3, timestampMs = 1699938000000),
    )
}
