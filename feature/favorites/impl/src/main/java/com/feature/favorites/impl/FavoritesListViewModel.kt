package com.feature.favorites.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.domain.model.FavoriteStock
import com.core.domain.repository.FavoritesRepository
import com.feature.favorites.impl.state.FavoriteStockUiModel
import com.feature.favorites.impl.state.FavoritesListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavoritesListViewModel @Inject constructor(
    favoritesRepository: FavoritesRepository,
) : ViewModel() {

    val uiState: StateFlow<FavoritesListState> =
        favoritesRepository.getAllFavorites()
            .map { favorites ->
                if (favorites.isEmpty()) {
                    FavoritesListState.Empty
                } else {
                    FavoritesListState.Content(favorites.map { it.toUiModel() })
                }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), FavoritesListState.Loading)
}

private fun FavoriteStock.toUiModel(): FavoriteStockUiModel =
    FavoriteStockUiModel(
        ticker = ticker,
        name = name,
        type = type,
        primaryExchange = primaryExchange,
    )
