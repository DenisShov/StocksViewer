package com.test.app.stockviewer.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.core.navigation.Navigator
import com.core.navigation.rememberNavigationState
import com.core.navigation.toDecoratedEntries
import com.feature.details.impl.ui.navigation.stockDetailsEntry
import com.feature.favorites.api.FavoritesListKey
import com.feature.favorites.impl.ui.navigation.favoritesListEntry
import com.feature.list.api.StocksListKey
import com.feature.list.impl.ui.navigation.stocksListEntry

@Composable
fun NavigationRoot() {
    val navigationState = rememberNavigationState(
        startRoute = StocksListKey,
        topLevelRoutes = setOf(StocksListKey, FavoritesListKey),
    )

    val navigator = remember { Navigator(navigationState) }

    BackHandler(enabled = navigator.canGoBack()) {
        navigator.onBackClick()
    }

    val entryProvider = entryProvider {
        stocksListEntry(navigator)
        stockDetailsEntry(navigator)
        favoritesListEntry(navigator)
    }

    val showBottomBar = navigationState.currentKey is FavoritesListKey ||
        navigationState.currentKey is StocksListKey

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    TopLevelTab.entries.forEach { tab ->
                        NavigationBarItem(
                            selected = navigationState.topLevelRoute == tab.key,
                            onClick = { navigator.navigate(tab.key) },
                            icon = {
                                Icon(
                                    painter = painterResource(
                                        id = if (navigationState.topLevelRoute == tab.key) {
                                            tab.selectedIcon
                                        } else {
                                            tab.unselectedIcon
                                        },
                                    ),
                                    contentDescription = tab.label,
                                )
                            },
                            label = { Text(tab.label) },
                        )
                    }
                }
            }
        },
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            NavDisplay(
                entries = navigationState.toDecoratedEntries(entryProvider),
                onBack = { navigator.onBackClick() },
            )
        }
    }
}
