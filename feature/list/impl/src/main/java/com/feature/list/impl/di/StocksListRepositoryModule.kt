package com.feature.list.impl.di

import com.feature.list.impl.data.repository.StocksListRepositoryImpl
import com.feature.list.impl.domain.repository.StocksListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface StocksListRepositoryModule {

    @Binds
    fun bindsCodeChallengesRepository(
        stocksListRepository: StocksListRepositoryImpl,
    ): StocksListRepository
}
