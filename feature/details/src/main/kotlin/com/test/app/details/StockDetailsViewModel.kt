package com.test.app.details

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.test.app.common.error.DomainError
import com.test.app.common.navigation.Screen
import com.test.app.details.model.CandleUiModel
import com.test.app.details.model.StockOverviewUiModel
import com.test.app.details.model.toUiModel
import com.test.app.domain.GetStockChartDataUseCase
import com.test.app.domain.GetStockOverviewByTickerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getStockOverviewByTickerUseCase: GetStockOverviewByTickerUseCase,
    private val getStockChartDataUseCase: GetStockChartDataUseCase,
) : ViewModel() {

    private val args = savedStateHandle.toRoute<Screen.StocksDetail>()

    @VisibleForTesting
    val ticker = args.stockTicker

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
