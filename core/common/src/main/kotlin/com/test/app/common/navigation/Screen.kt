package com.test.app.common.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class Screen : Parcelable {
    @Parcelize
    data object StocksList : Screen()

    @Parcelize
    data class StocksDetail(val stockTicker: String) : Screen()
}
