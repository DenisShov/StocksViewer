package com.feature.favorites.impl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feature.favorites.impl.ui.mapper.toUiModel
import com.feature.favorites.impl.ui.state.FavoritesListState
import com.sharedlibrary.favorites.domain.repository.FavoritesRepository
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
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                FavoritesListState.Loading
            )
}
