package com.feature.favorites.impl.ui

import app.cash.turbine.test
import com.core.testing.utils.CoroutineTestRule
import com.feature.favorites.impl.ui.state.FavoritesListState
import com.sharedlibrary.favorites.domain.model.FavoriteStock
import com.sharedlibrary.favorites.domain.repository.FavoritesRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesListViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule(UnconfinedTestDispatcher())

    private val favoritesFlow = MutableStateFlow<List<FavoriteStock>>(emptyList())
    private val favoritesRepository: FavoritesRepository = mockk()
    private lateinit var underTest: FavoritesListViewModel

    @Before
    fun setup() {
        every { favoritesRepository.getAllFavorites() } returns favoritesFlow
        underTest = FavoritesListViewModel(favoritesRepository)
    }

    @Test
    fun uiState_whenEmpty_thenShowsEmptyState() = runTest {
        underTest.uiState.test {
            val state = awaitItem()
            assertIs<FavoritesListState.Empty>(state)
        }
    }

    @Test
    fun uiState_whenFavoritesExist_thenShowsContentState() = runTest {
        favoritesFlow.value = testFavorites

        underTest.uiState.test {
            val state = awaitItem()
            assertIs<FavoritesListState.Content>(state)
            assertEquals(2, state.favorites.size)
            assertEquals("AAPL", state.favorites[0].ticker)
            assertEquals("Apple Inc.", state.favorites[0].name)
            assertEquals("GOOGL", state.favorites[1].ticker)
        }
    }

    @Test
    fun uiState_whenFavoritesAdded_thenUpdatesReactively() = runTest {
        underTest.uiState.test {
            val emptyState = awaitItem()
            assertIs<FavoritesListState.Empty>(emptyState)

            // Add favorites
            favoritesFlow.value = testFavorites

            val contentState = awaitItem()
            assertIs<FavoritesListState.Content>(contentState)
            assertEquals(2, contentState.favorites.size)
        }
    }

    @Test
    fun uiState_whenFavoritesRemoved_thenUpdatesReactively() = runTest {
        favoritesFlow.value = testFavorites

        underTest.uiState.test {
            val contentState = awaitItem()
            assertIs<FavoritesListState.Content>(contentState)

            // Remove all favorites
            favoritesFlow.value = emptyList()

            val emptyState = awaitItem()
            assertIs<FavoritesListState.Empty>(emptyState)
        }
    }

    @Test
    fun uiState_contentMapsFieldsCorrectly() = runTest {
        favoritesFlow.value = listOf(
            FavoriteStock(
                ticker = "MSFT",
                name = "Microsoft Corporation",
                type = "CS",
                primaryExchange = "XNAS",
            ),
        )

        underTest.uiState.test {
            val state = awaitItem()
            assertIs<FavoritesListState.Content>(state)
            val item = state.favorites.first()
            assertEquals("MSFT", item.ticker)
            assertEquals("Microsoft Corporation", item.name)
        }
    }
}

private val testFavorites = listOf(
    FavoriteStock(
        ticker = "AAPL",
        name = "Apple Inc.",
        type = "CS",
        primaryExchange = "XNAS",
    ),
    FavoriteStock(
        ticker = "GOOGL",
        name = "Alphabet Inc.",
        type = "CS",
        primaryExchange = "XNAS",
    ),
)
