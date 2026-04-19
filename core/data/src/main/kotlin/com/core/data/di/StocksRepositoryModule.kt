package com.core.data.di

import com.core.data.repository.StocksRepositoryImpl
import com.core.domain.repository.StocksRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface StocksRepositoryModule {

    @Binds
    fun bindsCodeChallengesRepository(
        stocksRepository: StocksRepositoryImpl,
    ): StocksRepository
}
