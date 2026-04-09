package com.test.app.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.app.details.impl.actions.ChartPeriod
import com.test.app.details.impl.model.toUiModel
import com.test.app.impl.model.toUiModel
import com.test.app.details.impl.state.StockDetailsState
import com.test.app.domain.GetStockChartDataUseCase
import com.test.app.domain.GetStockOverviewByTickerUseCase
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
    private val getStockOverviewByTickerUseCase: GetStockOverviewByTickerUseCase,
    private val getStockChartDataUseCase: GetStockChartDataUseCase,
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
            getStockOverviewByTickerUseCase.launch(ticker).fold(
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
                            error = error,
                            isLoading = false,
                        )
                    }
                },
            )
        }
    }

    fun getStockChartData(period: ChartPeriod) {
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
                            error = error,
                            isLoading = false,
                        )
                    }
                },
            )
        }
    }
}
