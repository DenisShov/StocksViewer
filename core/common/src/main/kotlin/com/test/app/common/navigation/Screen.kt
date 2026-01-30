package com.test.app.common.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen : NavKey {
    @Serializable
    data object StocksList : Screen, NavKey

    @Serializable
    data class StocksDetail(val stockTicker: String) : Screen, NavKey
}
