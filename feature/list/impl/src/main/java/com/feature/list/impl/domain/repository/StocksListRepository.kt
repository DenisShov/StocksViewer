package com.feature.list.impl.domain.repository

import arrow.core.Either
import com.core.common.error.DomainError
import com.feature.list.impl.domain.model.Tickers

interface StocksListRepository {

    suspend fun getStockList(cursor: String?): Either<DomainError, Tickers>

    suspend fun searchStockByQuery(
        searchQuery: String,
        cursor: String?,
    ): Either<DomainError, Tickers>
}
