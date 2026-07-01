package com.feature.favorites.impl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feature.favorites.impl.ui.mapper.toUiModel
import com.feature.favorites.impl.ui.state.FavoritesListState
import com.sharedlibrary.favorites.domain.repository.FavoritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesListViewModel @Inject constructor(
    val favoritesRepository: FavoritesRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<FavoritesListState>(FavoritesListState.Loading)
    val uiState: StateFlow<FavoritesListState> by lazy {
        collectFavorites()
        _uiState.asStateFlow()
    }

    private fun collectFavorites() {
        viewModelScope.launch {
            favoritesRepository.getAllFavorites()
                .map { favorites ->
                    if (favorites.isEmpty()) {
                        FavoritesListState.Empty
                    } else {
                        FavoritesListState.Content(
                            favorites.map { it.toUiModel() }.toImmutableList()
                        )
                    }
                }
                .collect { state ->
                    _uiState.value = state
                }
        }
    }
}
