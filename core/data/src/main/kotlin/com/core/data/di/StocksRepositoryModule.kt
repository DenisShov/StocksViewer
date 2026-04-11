package com.core.data.di

import com.core.data.repository.StocksRepository
import com.core.data.repository.StocksRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface StocksRepositoryModule {
    @Binds
    fun bindsCodeChallengesRepository(
        stocksRepository: StocksRepositoryImpl,
    ): StocksRepository
}
