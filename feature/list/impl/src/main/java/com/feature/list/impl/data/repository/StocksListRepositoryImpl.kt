package com.feature.list.impl.data.repository

import arrow.core.Either
import com.core.common.error.DomainError
import com.core.network.model.errors.mapLeftToDomainError
import com.core.network.retrofit.StocksApi
import com.feature.list.impl.domain.mapper.toDomain
import com.feature.list.impl.domain.model.Tickers
import com.feature.list.impl.domain.repository.StocksListRepository
import javax.inject.Inject

class StocksListRepositoryImpl @Inject constructor(
    private val stocksApi: StocksApi,
) : StocksListRepository {

    override suspend fun searchStockByQuery(
        searchQuery: String,
        cursor: String?,
    ): Either<DomainError, Tickers> =
        stocksApi.searchStockByQuery(searchQuery = searchQuery, cursor = cursor)
            .mapLeftToDomainError().map { it.toDomain() }

    override suspend fun getStockList(cursor: String?): Either<DomainError, Tickers> =
        stocksApi.getStockList(cursor = cursor).mapLeftToDomainError().map { it.toDomain() }

}
