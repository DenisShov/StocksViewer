package com.feature.favorites.impl.di

import com.feature.favorites.impl.ui.FavoritesListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val favoritesListModule = module {
    viewModel { FavoritesListViewModel(get()) }
}
