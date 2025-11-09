package com.test.app.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.test.app.data.repository.StocksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StocksListViewModel @Inject constructor(
    stocksRepository: StocksRepository,
) : ViewModel() {

    val stocksPaging = stocksRepository.getStocksFlow()
        .cachedIn(viewModelScope)

}
