package com.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_stocks")
data class FavoriteStockEntity(
    @PrimaryKey
    val ticker: String,
    val name: String,
    val type: String,
    val primaryExchange: String,
)
