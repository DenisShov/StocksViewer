package com.feature.list.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.feature.list.impl.model.toUiModel
import com.feature.list.impl.paging.StocksSearchPager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class StocksListViewModel @Inject constructor(
    stocksSearchPager: StocksSearchPager,
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    val stocksPaging = searchQuery
        .debounce(1000) // avoid firing on every keystroke
        .distinctUntilChanged()
        .flatMapLatest { query ->
            stocksSearchPager.getPager(query)
                .distinctUntilChanged()
                .map { pagingData -> pagingData.map { it.toUiModel() } }
        }
        .cachedIn(viewModelScope)

    fun onSearchQueryChange(query: String) {
        searchQuery.value = query
    }

}
