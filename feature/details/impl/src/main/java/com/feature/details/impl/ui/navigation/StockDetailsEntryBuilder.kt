package com.feature.details.impl.ui.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.core.navigation.Navigator
import com.feature.details.api.StocksDetailKey
import com.feature.details.impl.ui.StockDetailsViewModel
import com.feature.details.impl.ui.compose.StockDetailsRoute

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
