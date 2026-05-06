package com.feature.details.impl.ui.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.core.navigation.Navigator
import com.feature.details.api.StocksDetailKey
import com.feature.details.impl.ui.StockDetailsViewModel
import com.feature.details.impl.ui.compose.StockDetailsRoute
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

fun EntryProviderScope<NavKey>.stockDetailsEntry(
    navigator: Navigator,
) {
    entry<StocksDetailKey> { key ->
        val viewModel = koinViewModel<StockDetailsViewModel> { parametersOf(key.stockTicker) }
        StockDetailsRoute(
            viewModel = viewModel,
            onBackButtonClick = navigator::onBackClick,
        )
    }
}
