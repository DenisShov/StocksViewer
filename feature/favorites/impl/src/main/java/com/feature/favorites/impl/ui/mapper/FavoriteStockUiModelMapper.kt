package com.feature.favorites.impl.ui.mapper

import com.feature.favorites.impl.ui.state.FavoriteStockUiModel
import com.sharedlibrary.favorites.domain.model.FavoriteStock

fun FavoriteStock.toUiModel(): FavoriteStockUiModel =
    FavoriteStockUiModel(
        ticker = ticker,
        name = name,
        type = getType(),
    )

private fun FavoriteStock.getType(): String = when (type) {
    "CS" -> "Common Stock"
    "ETF" -> "Exchange Traded Fund"
    "ADRC" -> "Depositary Receipt"
    else -> type
}
