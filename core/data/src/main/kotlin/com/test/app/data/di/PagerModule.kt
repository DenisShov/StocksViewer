package com.test.app.data.di

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.test.app.data.paging.StocksPagingSource
import com.test.app.model.data.Ticker
import com.test.app.network.retrofit.StocksApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PagerModule {

    @Provides
    @Singleton
    fun providePager(stocksApi: StocksApi): Pager<String, Ticker> {
        return Pager(
            PagingConfig(pageSize = 2)
        ) {
            StocksPagingSource(stocksApi)
        }
    }
}
