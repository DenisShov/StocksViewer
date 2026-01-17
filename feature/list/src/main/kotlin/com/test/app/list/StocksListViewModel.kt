package com.test.app.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.test.app.data.repository.StocksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class StocksListViewModel @Inject constructor(
    stocksRepository: StocksRepository,
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    val stocksPaging = searchQuery
        .debounce(1000) // avoid firing on every keystroke
        .distinctUntilChanged()
        .flatMapLatest { query ->
            stocksRepository.getStocksFlow(query)
        }
        .cachedIn(viewModelScope)

    fun onSearchQueryChange(query: String) {
        searchQuery.value = query
    }

}
