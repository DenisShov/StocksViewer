package com.sharedlibrary.favorites.data.repository

import com.core.database.dao.FavoriteStockDao
import com.sharedlibrary.favorites.data.mapper.toDomain
import com.sharedlibrary.favorites.data.mapper.toEntity
import com.sharedlibrary.favorites.domain.model.FavoriteStock
import com.sharedlibrary.favorites.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
class FavoritesRepositoryImpl(
    private val dao: FavoriteStockDao,
) : FavoritesRepository {

    override fun getAllFavorites(): Flow<List<FavoriteStock>> =
        dao.getAllFavorites().map { entities ->
            entities.map { it.toDomain() }
        }

    override fun isFavorite(ticker: String): Flow<Boolean> =
        dao.isFavorite(ticker)

    override suspend fun addFavorite(stock: FavoriteStock) =
        dao.upsertFavorite(stock.toEntity())

    override suspend fun removeFavorite(ticker: String) =
        dao.deleteFavoriteByTicker(ticker)
}
