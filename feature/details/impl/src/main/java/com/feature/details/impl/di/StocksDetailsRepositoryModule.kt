package com.feature.details.impl.di

import com.feature.details.impl.data.repository.StocksDetailsRepositoryImpl
import com.feature.details.impl.domain.repository.StocksDetailsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface StocksDetailsRepositoryModule {

    @Binds
    fun bindsCodeChallengesRepository(
        stocksDetailsRepository: StocksDetailsRepositoryImpl,
    ): StocksDetailsRepository
}
