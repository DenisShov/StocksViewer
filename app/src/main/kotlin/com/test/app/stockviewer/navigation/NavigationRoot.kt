package com.test.app.stockviewer.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.core.navigation.Navigator
import com.feature.details.impl.navigation.stockDetailsEntry
import com.feature.list.api.StocksListKey
import com.feature.list.impl.navigation.stocksListEntry

@Composable
fun NavigationRoot() {
    val backStack = rememberNavBackStack(StocksListKey)

    BackHandler(enabled = backStack.size > 1) {
        backStack.removeAt(backStack.lastIndex)
    }

    val navigator = remember { Navigator(backStack = backStack) }

    val entryProvider = entryProvider {
        stockDetailsEntry(navigator)
        stocksListEntry(navigator)
    }

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider,
    )
}
