package com.shared_library.favorites.domain.model

data class FavoriteStock(
    val ticker: String,
    val name: String,
    val type: String,
    val primaryExchange: String,
)
