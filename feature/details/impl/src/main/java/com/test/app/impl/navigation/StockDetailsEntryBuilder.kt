package com.test.app.impl.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.test.app.details.api.StocksDetailKey
import com.test.app.impl.StockDetailsViewModel
import com.test.app.impl.compose.StockDetailsRoute
import com.test.app.navigation.Navigator

fun EntryProviderScope<NavKey>.stockDetailsEntry(
    navigator: Navigator,
) {
    entry<StocksDetailKey> { key ->
        val viewModel =
            hiltViewModel<StockDetailsViewModel, StockDetailsViewModel.Factory> { factory ->
                factory.create(ticker = key.stockTicker)
            }
        StockDetailsRoute(
            viewModel = viewModel,
            onBackButtonClick = navigator::onBackClick,
        )
    }
}
