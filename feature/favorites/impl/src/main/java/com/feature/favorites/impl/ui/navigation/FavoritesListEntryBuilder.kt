package com.feature.favorites.impl.ui.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.core.navigation.Navigator
import com.feature.details.api.StocksDetailKey
import com.feature.favorites.api.FavoritesListKey
import com.feature.favorites.impl.ui.FavoritesListRoute

fun EntryProviderScope<NavKey>.favoritesListEntry(
    navigator: Navigator,
) {
    entry<FavoritesListKey> {
        FavoritesListRoute(
            onStockClick = { ticker ->
                navigator.navigate(StocksDetailKey(ticker))
            },
        )
    }
}
