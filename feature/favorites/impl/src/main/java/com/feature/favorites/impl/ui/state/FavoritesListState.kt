package com.feature.favorites.impl.ui.state

sealed interface FavoritesListState {
    data object Loading : FavoritesListState
    data object Empty : FavoritesListState
    data class Content(val favorites: List<FavoriteStockUiModel>) : FavoritesListState
}

data class FavoriteStockUiModel(
    val ticker: String,
    val name: String,
    val type: String,
    val primaryExchange: String,
)
