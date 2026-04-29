package com.shared_library.favorites.di

import com.shared_library.favorites.data.repository.FavoritesRepositoryImpl
import com.shared_library.favorites.domain.repository.FavoritesRepository
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
