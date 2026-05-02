package com.feature.favorites.impl.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.feature.favorites.impl.ui.compose.FavoritesListScreen
import com.feature.favorites.impl.ui.state.FavoriteStockUiModel
import com.feature.favorites.impl.ui.state.FavoritesListState
import org.junit.Rule
import org.junit.Test

class FavoritesListScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val testFavorites = listOf(
        FavoriteStockUiModel(ticker = "AAPL", name = "Apple Inc.", type = "Common Stock"),
        FavoriteStockUiModel(ticker = "GOOGL", name = "Alphabet Inc.", type = "Common Stock"),
        FavoriteStockUiModel(ticker = "MSFT", name = "Microsoft Corporation", type = "Common Stock"),
    )

    @Test
    fun whenLoading_thenShowsLoadingIndicator() {
        composeTestRule.setContent {
            FavoritesListScreen(
                uiState = FavoritesListState.Loading,
                onStockClick = {},
            )
        }

        composeTestRule
            .onNodeWithTag("favorites_loading")
            .assertIsDisplayed()
    }

    @Test
    fun whenLoading_thenListIsNotDisplayed() {
        composeTestRule.setContent {
            FavoritesListScreen(
                uiState = FavoritesListState.Loading,
                onStockClick = {},
            )
        }

        composeTestRule
            .onNodeWithTag("favorites_list")
            .assertDoesNotExist()
    }

    @Test
    fun whenEmpty_thenShowsEmptyMessage() {
        composeTestRule.setContent {
            FavoritesListScreen(
                uiState = FavoritesListState.Empty,
                onStockClick = {},
            )
        }

        composeTestRule
            .onNodeWithTag("favorites_empty")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("No favorites yet. Tap the star icon on a stock to add it here.")
            .assertIsDisplayed()
    }

    @Test
    fun whenEmpty_thenListIsNotDisplayed() {
        composeTestRule.setContent {
            FavoritesListScreen(
                uiState = FavoritesListState.Empty,
                onStockClick = {},
            )
        }

        composeTestRule
            .onNodeWithTag("favorites_list")
            .assertDoesNotExist()
    }

    @Test
    fun whenContent_thenShowsList() {
        composeTestRule.setContent {
            FavoritesListScreen(
                uiState = FavoritesListState.Content(testFavorites),
                onStockClick = {},
            )
        }

        composeTestRule
            .onNodeWithTag("favorites_list")
            .assertIsDisplayed()
    }

    @Test
    fun whenContent_thenShowsTitle() {
        composeTestRule.setContent {
            FavoritesListScreen(
                uiState = FavoritesListState.Content(testFavorites),
                onStockClick = {},
            )
        }

        composeTestRule
            .onNodeWithTag("favorites_title")
            .assertIsDisplayed()
    }

    @Test
    fun whenContent_thenDisplaysStockNames() {
        composeTestRule.setContent {
            FavoritesListScreen(
                uiState = FavoritesListState.Content(testFavorites),
                onStockClick = {},
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
    fun whenContent_thenDisplaysStockItems() {
        composeTestRule.setContent {
            FavoritesListScreen(
                uiState = FavoritesListState.Content(testFavorites),
                onStockClick = {},
            )
        }

        composeTestRule
            .onNodeWithTag("favorite_stock_item_AAPL")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("favorite_stock_item_GOOGL")
            .assertIsDisplayed()
    }

    @Test
    fun whenStockItemClicked_thenCallsOnStockClick() {
        var clickedTicker: String? = null

        composeTestRule.setContent {
            FavoritesListScreen(
                uiState = FavoritesListState.Content(testFavorites),
                onStockClick = { clickedTicker = it },
            )
        }

        composeTestRule
            .onNodeWithTag("favorite_stock_item_AAPL")
            .performClick()

        assert(clickedTicker == "AAPL")
    }

    @Test
    fun whenDifferentStockClicked_thenCallsWithCorrectTicker() {
        var clickedTicker: String? = null

        composeTestRule.setContent {
            FavoritesListScreen(
                uiState = FavoritesListState.Content(testFavorites),
                onStockClick = { clickedTicker = it },
            )
        }

        composeTestRule
            .onNodeWithTag("favorite_stock_item_GOOGL")
            .performClick()

        assert(clickedTicker == "GOOGL")
    }
}
