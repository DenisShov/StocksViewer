package com.feature.favorites.impl.data

import com.core.database.dao.FavoriteStockDao
import com.core.domain.model.FavoriteStock
import com.core.domain.repository.FavoritesRepository
import com.feature.favorites.impl.data.mapper.toDomain
import com.feature.favorites.impl.data.mapper.toEntity
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
