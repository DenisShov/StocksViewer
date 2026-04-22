package com.feature.details.impl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.common.mapper.ErrorMapper
import com.feature.details.impl.domain.repository.StocksDetailsRepository
import com.feature.details.impl.domain.usecase.GetStockChartDataUseCase
import com.feature.details.impl.ui.actions.ChartPeriod
import com.feature.details.impl.ui.model.toUiModel
import com.feature.details.impl.ui.state.StockDetailsState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = StockDetailsViewModel.Factory::class)
class StockDetailsViewModel @AssistedInject constructor(
    @Assisted
    private val ticker: String,
    private val stocksDetailsRepository: StocksDetailsRepository,
    private val getStockChartDataUseCase: GetStockChartDataUseCase,
    private val errorMapper: ErrorMapper,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(ticker: String): StockDetailsViewModel
    }

    private val _uiState = MutableStateFlow(StockDetailsState())
    val uiState: StateFlow<StockDetailsState> by lazy {
        getStockOverviewByTicker()
        _uiState.asStateFlow()
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

    fun getStockChartData(period: ChartPeriod) {
        _uiState.update {
            it.copy(
                selectedPeriod = period,
                isLoading = true,
            )
        }
        viewModelScope.launch {
            getStockChartDataUseCase.launch(ticker, period.value).fold(
                ifRight = { stockChart ->
                    _uiState.update {
                        it.copy(
                            candles = stockChart.results.map { result -> result.toUiModel() },
                            isLoading = false,
                        )
                    }
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
}
