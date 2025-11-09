package com.test.app.data.di

import com.test.app.data.repository.StocksRepository
import com.test.app.data.repository.StocksRepositoryImpl
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
