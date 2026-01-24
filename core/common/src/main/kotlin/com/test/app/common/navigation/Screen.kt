package com.test.app.common.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object StocksList : Screen()

    @Serializable
    data class StocksDetail(val stockTicker: String) : Screen()
}
