package com.shared_library.favorites.data.repository

import com.core.database.dao.FavoriteStockDao
import com.shared_library.favorites.data.mapper.toDomain
import com.shared_library.favorites.data.mapper.toEntity
import com.shared_library.favorites.domain.model.FavoriteStock
import com.shared_library.favorites.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
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
