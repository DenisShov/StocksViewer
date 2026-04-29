package com.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.core.database.entity.FavoriteStockEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteStockDao {

    @Query("SELECT * FROM favorite_stocks ORDER BY name ASC")
    fun getAllFavorites(): Flow<List<FavoriteStockEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_stocks WHERE ticker = :ticker)")
    fun isFavorite(ticker: String): Flow<Boolean>

    @Upsert
    suspend fun upsertFavorite(entity: FavoriteStockEntity)

    @Query("DELETE FROM favorite_stocks WHERE ticker = :ticker")
    suspend fun deleteFavoriteByTicker(ticker: String)
}
