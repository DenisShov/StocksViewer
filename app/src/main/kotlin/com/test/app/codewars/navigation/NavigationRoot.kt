package com.test.app.codewars.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.test.app.common.navigation.Screen
import com.test.app.details.StockDetailsRoute
import com.test.app.details.StockDetailsViewModel
import com.test.app.list.StocksListRoute

@Composable
fun NavigationRoot() {
    val backStack = rememberNavBackStack(Screen.StocksList)

    BackHandler(enabled = backStack.size > 1) {
        backStack.removeAt(backStack.lastIndex)
    }

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = { key ->
            when (key) {
                is Screen.StocksList -> {
                    NavEntry(key) {
                        StocksListRoute(
                            onStockClick = { stockTicker ->
                                backStack.add(Screen.StocksDetail(stockTicker))
                            })
                    }
                }

                is Screen.StocksDetail -> {
                    NavEntry(key) {
                        val viewModel =
                            hiltViewModel<StockDetailsViewModel, StockDetailsViewModel.Factory> { factory ->
                                factory.create(ticker = key.stockTicker)
                            }

                        StockDetailsRoute(
                            viewModel = viewModel,
                            onBackButtonClick = {
                                if (backStack.size > 1) {
                                    backStack.removeAt(backStack.lastIndex)
                                }
                            })
                    }
                }

                else -> {
                    throw IllegalArgumentException("Invalid key: $key")
                }
            }
        }
    )
}
