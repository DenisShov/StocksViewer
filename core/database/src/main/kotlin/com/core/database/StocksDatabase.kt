package com.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.core.database.dao.FavoriteStockDao
import com.core.database.entity.FavoriteStockEntity

@Database(
    entities = [FavoriteStockEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class StocksDatabase : RoomDatabase() {
    abstract fun favoriteStockDao(): FavoriteStockDao
}
