package com.test.app.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.test.app.details.api.StocksDetailKey
import com.test.app.impl.compose.StocksListRoute
import com.test.app.list.api.StocksListKey
import com.test.app.navigation.Navigator

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
