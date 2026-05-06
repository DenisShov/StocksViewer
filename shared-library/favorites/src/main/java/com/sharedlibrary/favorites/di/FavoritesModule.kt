package com.sharedlibrary.favorites.di

import com.sharedlibrary.favorites.data.repository.FavoritesRepositoryImpl
import com.sharedlibrary.favorites.domain.repository.FavoritesRepository
import org.koin.dsl.module

val favoritesModule = module {
    single<FavoritesRepository> { FavoritesRepositoryImpl(get()) }
}
