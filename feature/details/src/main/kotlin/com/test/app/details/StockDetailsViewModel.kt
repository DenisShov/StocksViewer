package com.test.app.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.app.common.error.DomainError
import com.test.app.details.model.CandleUiModel
import com.test.app.details.model.StockOverviewUiModel
import com.test.app.details.model.toUiModel
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

    private val _uiState = MutableStateFlow(State(stockDetailsState = StockDetailsState.Loading))
    val uiState: StateFlow<State> by lazy {
        getStockOverviewByTicker()
        _uiState.asStateFlow()
    }

    fun getStockOverviewByTicker() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(stockDetailsState = StockDetailsState.Loading)
            }
            getStockOverviewByTickerUseCase.launch(ticker).fold(
                ifRight = { stockOverview ->
                    _uiState.update {
                        it.copy(
                            stockDetailsState = StockDetailsState.Success(
                                stockOverview = stockOverview.toUiModel()
                            )
                        )
                    }
                    getStockChartData("week")
                },
                ifLeft = { error ->
                    _uiState.update {
                        it.copy(stockDetailsState = StockDetailsState.Error(error))
                    }
                },
            )
        }
    }

    fun getStockChartData(period: String) {
        viewModelScope.launch {
            getStockChartDataUseCase.launch(ticker, period).fold(
                    ifRight = { stockChart ->
                        val stockDetailsState =
                            (_uiState.value.stockDetailsState as? StockDetailsState.Success)
                        stockDetailsState?.let {
                            _uiState.update {
                                it.copy(
                                    stockDetailsState = stockDetailsState.copy(
                                    candles = stockChart.results.map { result -> result.toUiModel() }))
                            }
                        }
                    },
                    ifLeft = { error ->
                        _uiState.update {
                            it.copy(stockDetailsState = StockDetailsState.Error(error))
                        }
                    },
                )
        }
    }

    data class State(
        val stockDetailsState: StockDetailsState,
    )

    sealed interface StockDetailsState {
        data class Success(
            val stockOverview: StockOverviewUiModel, val candles: List<CandleUiModel> = emptyList()
        ) : StockDetailsState

        data class Error(val error: DomainError) : StockDetailsState

        data object Loading : StockDetailsState
    }
}
