package com.feature.favorites.impl.data.mapper

import com.core.database.entity.FavoriteStockEntity
import com.core.domain.model.FavoriteStock

fun FavoriteStockEntity.toDomain(): FavoriteStock =
    FavoriteStock(
        ticker = ticker,
        name = name,
        type = type,
        primaryExchange = primaryExchange,
    )

fun FavoriteStock.toEntity(): FavoriteStockEntity =
    FavoriteStockEntity(
        ticker = ticker,
        name = name,
        type = type,
        primaryExchange = primaryExchange,
    )
