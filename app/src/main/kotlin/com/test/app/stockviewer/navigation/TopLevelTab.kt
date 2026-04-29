package com.test.app.stockviewer.navigation

import androidx.annotation.DrawableRes
import androidx.navigation3.runtime.NavKey
import com.core.designsystem.R
import com.feature.favorites.api.FavoritesListKey
import com.feature.list.api.StocksListKey

enum class TopLevelTab(
    val key: NavKey,
    val label: String,
    @param:DrawableRes val selectedIcon: Int,
    @param:DrawableRes val unselectedIcon: Int,
) {
    STOCKS(
        key = StocksListKey,
        label = "Stocks",
        selectedIcon = R.drawable.ic_home_filled,
        unselectedIcon = R.drawable.ic_home_outline,
    ),
    FAVORITES(
        key = FavoritesListKey,
        label = "Favorites",
        selectedIcon = R.drawable.ic_star_filled,
        unselectedIcon = R.drawable.ic_star_outline,
    ),
}
