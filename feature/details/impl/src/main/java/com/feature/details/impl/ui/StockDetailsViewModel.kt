package com.feature.details.impl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.common.mapper.ErrorMapper
import com.feature.details.impl.domain.repository.StocksDetailsRepository
import com.feature.details.impl.domain.usecase.GetStockChartDataUseCase
import com.feature.details.impl.ui.actions.ChartPeriod
import com.feature.details.impl.ui.model.toUiModel
import com.feature.details.impl.ui.state.StockDetailsState
import com.sharedlibrary.favorites.domain.model.FavoriteStock
import com.sharedlibrary.favorites.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StockDetailsViewModel(
    private val ticker: String,
    private val stocksDetailsRepository: StocksDetailsRepository,
    private val getStockChartDataUseCase: GetStockChartDataUseCase,
    private val favoritesRepository: FavoritesRepository,
    private val errorMapper: ErrorMapper,
) : ViewModel() {

    private val _uiState = MutableStateFlow(StockDetailsState())
    val uiState: StateFlow<StockDetailsState> by lazy {
        collectFavoriteStatus()
        getStockOverviewByTicker()
        _uiState.asStateFlow()
    }

    private fun collectFavoriteStatus() {
        viewModelScope.launch {
            favoritesRepository.isFavorite(ticker).collect { isFav ->
                _uiState.update { it.copy(isFavorite = isFav) }
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            if (_uiState.value.isFavorite) {
                favoritesRepository.removeFavorite(ticker)
            } else {
                _uiState.value.stockOverview?.let {
                    favoritesRepository.addFavorite(
                        FavoriteStock(
                            ticker = ticker,
                            name = it.name,
                            type = it.type,
                            primaryExchange = it.exchange,
                        )
                    )
                }
            }
        }
    }

    fun getStockOverviewByTicker() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                )
            }
            stocksDetailsRepository.getStockOverviewByTicker(ticker).fold(
                ifRight = { stockOverview ->
                    _uiState.update {
                        it.copy(
                            stockOverview = stockOverview.toUiModel(),
                            errorString = null,
                            isLoading = false,
                        )
                    }
                    getStockChartData(ChartPeriod.WEEK)
                },
                ifLeft = { error ->
                    _uiState.update {
                        it.copy(
                            errorString = errorMapper.mapToStringError(error),
                            isLoading = false,
                        )
                    }
                },
            )
        }
    }

    fun retryGetStockChartData() {
        getStockChartData(_uiState.value.selectedPeriod)
    }

    fun getStockChartData(period: ChartPeriod) {
        _uiState.update {
            it.copy(
                selectedPeriod = period,
                isChartLoading = true,
            )
        }
        viewModelScope.launch {
            getStockChartDataUseCase.launch(ticker, period.value).fold(
                ifRight = { stockChart ->
                    _uiState.update {
                        it.copy(
                            candles = stockChart.results.map { result -> result.toUiModel() },
                            chartErrorString = null,
                            isChartLoading = false,
                        )
                    }
                },
                ifLeft = { error ->
                    _uiState.update {
                        it.copy(
                            chartErrorString = errorMapper.mapToStringError(error),
                            isChartLoading = false,
                        )
                    }
                },
            )
        }
    }
}
