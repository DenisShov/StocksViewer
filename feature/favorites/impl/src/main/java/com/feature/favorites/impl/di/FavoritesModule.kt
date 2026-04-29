package com.feature.favorites.impl.di

import com.core.domain.repository.FavoritesRepository
import com.feature.favorites.impl.data.FavoritesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface FavoritesModule {

    @Binds
    fun bindsFavoritesRepository(
        favoritesRepositoryImpl: FavoritesRepositoryImpl,
    ): FavoritesRepository
}
