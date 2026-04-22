package com.feature.list.impl.ui.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.core.navigation.Navigator
import com.feature.details.api.StocksDetailKey
import com.feature.list.api.StocksListKey
import com.feature.list.impl.ui.compose.StocksListRoute

fun EntryProviderScope<NavKey>.stocksListEntry(
    navigator: Navigator,
) {
    entry<StocksListKey> {
        StocksListRoute(
            onStockClick = { ticker ->
                navigator.navigate(StocksDetailKey(ticker))
            },
        )
    }
}
