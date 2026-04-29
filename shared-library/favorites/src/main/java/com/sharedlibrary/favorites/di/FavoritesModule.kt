package com.sharedlibrary.favorites.di

import com.sharedlibrary.favorites.data.repository.FavoritesRepositoryImpl
import com.sharedlibrary.favorites.domain.repository.FavoritesRepository
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
