package com.feature.favorites.impl.ui.mapper

import com.feature.favorites.impl.ui.state.FavoriteStockUiModel
import com.sharedlibrary.favorites.domain.model.FavoriteStock

fun FavoriteStock.toUiModel(): FavoriteStockUiModel =
    FavoriteStockUiModel(
        ticker = ticker,
        name = name,
        type = type,
        primaryExchange = primaryExchange,
    )
