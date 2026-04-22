package com.feature.list.impl.ui

import androidx.paging.PagingData
import app.cash.turbine.test
import com.core.testing.data.TEST_EXCHANGE
import com.core.testing.data.TEST_LOCALE
import com.core.testing.data.TEST_MARKET
import com.core.testing.data.testStockOverviewLists
import com.core.testing.utils.CoroutineTestRule
import com.feature.list.impl.domain.model.Ticker
import com.feature.list.impl.ui.paging.StocksSearchPager
import io.mockk.Ordering
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class StocksListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule(testDispatcher)

    private val stocksSearchPager: StocksSearchPager = mockk()
    private lateinit var underTest: StocksListViewModel

    @Before
    fun setup() {
        underTest = StocksListViewModel(stocksSearchPager)

        every { stocksSearchPager.getPager(any()) } returns
            flowOf(PagingData.Companion.from(testStockOverviewLists))
    }

    @Test
    fun whenInitialized_thenEmitsPagingData() = runTest {
        underTest.stocksPaging.test {
            advanceTimeBy(1100)
            val pagingData = expectMostRecentItem()
            assertNotNull(pagingData)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun whenInitialized_thenCallsPagerWithEmptyQuery() = runTest {
        underTest.stocksPaging.test {
            advanceTimeBy(1100)
            expectMostRecentItem()
            cancelAndIgnoreRemainingEvents()
        }

        verify { stocksSearchPager.getPager("") }
    }

    @Test
    fun whenSearchQueryChanges_thenCallsPagerWithQuery() = runTest {
        val searchResults = listOf(
            Ticker(
                ticker = "GOOGL",
                name = "Alphabet Inc.",
                market = TEST_MARKET,
                locale = TEST_LOCALE,
                primaryExchange = TEST_EXCHANGE,
                type = "CS",
                active = true,
            ),
        )
        every { stocksSearchPager.getPager("GOOGL") } returns
            flowOf(PagingData.Companion.from(searchResults))

        underTest.stocksPaging.test {
            advanceTimeBy(1100)
            expectMostRecentItem()

            underTest.onSearchQueryChange("GOOGL")
            advanceTimeBy(1100)

            val pagingData = expectMostRecentItem()
            assertNotNull(pagingData)
            cancelAndIgnoreRemainingEvents()
        }

        verify { stocksSearchPager.getPager("GOOGL") }
    }

    @Test
    fun whenSearchQueryCleared_thenCallsPagerWithEmptyQuery() = runTest {
        every { stocksSearchPager.getPager("test") } returns
            flowOf(PagingData.Companion.from(testStockOverviewLists))

        underTest.stocksPaging.test {
            advanceTimeBy(1100)
            expectMostRecentItem()

            underTest.onSearchQueryChange("test")
            advanceTimeBy(1100)
            expectMostRecentItem()

            underTest.onSearchQueryChange("")
            advanceTimeBy(1100)
            expectMostRecentItem()

            cancelAndIgnoreRemainingEvents()
        }

        verify(ordering = Ordering.ORDERED) {
            stocksSearchPager.getPager("")
            stocksSearchPager.getPager("test")
            stocksSearchPager.getPager("")
        }
    }

    @Test
    fun whenSameQueryRepeated_thenDebouncesProperly() = runTest {
        underTest.stocksPaging.test {
            advanceTimeBy(1100)
            expectMostRecentItem()

            // Rapid-fire the same query — debounce + distinctUntilChanged should collapse these
            underTest.onSearchQueryChange("A")
            advanceTimeBy(200)
            underTest.onSearchQueryChange("AP")
            advanceTimeBy(200)
            underTest.onSearchQueryChange("APP")
            advanceTimeBy(1100)

            expectMostRecentItem()
            cancelAndIgnoreRemainingEvents()
        }

        // Only the final debounced value should reach the pager
        verify(exactly = 1) { stocksSearchPager.getPager("APP") }
        verify(exactly = 0) { stocksSearchPager.getPager("A") }
        verify(exactly = 0) { stocksSearchPager.getPager("AP") }
    }

    @Test
    fun whenEmptyResults_thenStillEmitsPagingData() = runTest {
        every { stocksSearchPager.getPager("xyz") } returns
            flowOf(PagingData.Companion.from(emptyList()))

        underTest.stocksPaging.test {
            advanceTimeBy(1100)
            expectMostRecentItem()

            underTest.onSearchQueryChange("xyz")
            advanceTimeBy(1100)

            val pagingData = expectMostRecentItem()
            assertNotNull(pagingData)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
