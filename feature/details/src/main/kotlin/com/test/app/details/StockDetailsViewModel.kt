package com.test.app.details

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.app.common.error.AppError
import com.test.app.common.result.fold
import com.test.app.details.model.CandleUiModel
import com.test.app.details.model.StockOverviewUiModel
import com.test.app.details.model.toUiModel
import com.test.app.domain.GetStockChartDataUseCase
import com.test.app.domain.GetStockOverviewByTickerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getStockOverviewByTickerUseCase: GetStockOverviewByTickerUseCase,
    private val getStockChartDataUseCase: GetStockChartDataUseCase,
) : ViewModel() {

    private val args: StockDetailArgs = StockDetailArgs(savedStateHandle)

    @VisibleForTesting
    val ticker = args.tickerArgument

    private val _uiState = MutableStateFlow(State(stockDetailsState = StockDetailsState.Loading))
    val uiState: StateFlow<State> by lazy {
        getStockOverviewByTicker()
        _uiState.asStateFlow()
    }

    fun getStockOverviewByTicker() {
        viewModelScope.launch {
            getStockOverviewByTickerUseCase.launch(ticker).onEach { result ->
                result.fold(
                    onSuccess = { stockOverview ->
                        _uiState.update {
                            it.copy(
                                stockDetailsState = StockDetailsState.Success(
                                    stockOverview = stockOverview.toUiModel()
                                )
                            )
                        }
                        getStockChartData("week")
                    },
                    onFailure = { error ->
                        _uiState.update {
                            it.copy(stockDetailsState = StockDetailsState.Error(error))
                        }
                    },
                    onLoading = {
                        _uiState.update {
                            it.copy(stockDetailsState = StockDetailsState.Loading)
                        }
                    })
            }.launchIn(viewModelScope)
        }
    }

    fun getStockChartData(period: String) {
        viewModelScope.launch {
            getStockChartDataUseCase.launch(ticker, period).onEach { result ->
                result.fold(
                    onSuccess = { stockChart ->
                        val stockDetailsState =
                            (_uiState.value.stockDetailsState as? StockDetailsState.Success)
                        stockDetailsState?.let {
                            _uiState.update {
                                it.copy(
                                    stockDetailsState = stockDetailsState.copy(
                                        candles = stockChart.results.map { result -> result.toUiModel() }
                                    )
                                )
                            }
                        }
                    },
                    onFailure = { error ->
                        _uiState.update {
                            it.copy(stockDetailsState = StockDetailsState.Error(error))
                        }
                    },
                    onLoading = {
                        // do nothing
                    })
            }.launchIn(viewModelScope)
        }
    }

    data class State(
        val stockDetailsState: StockDetailsState,
    )

    sealed interface StockDetailsState {
        data class Success(
            val stockOverview: StockOverviewUiModel,
            val candles: List<CandleUiModel> = emptyList()
        ) : StockDetailsState

        data class Error(val error: AppError) : StockDetailsState

        data object Loading : StockDetailsState
    }

    companion object {
        const val STOCK_TICKER_ARG = "stock_ticker_arg"
    }

    internal class StockDetailArgs(val tickerArgument: String) {
        constructor(savedStateHandle: SavedStateHandle) : this(
            checkNotNull(
                savedStateHandle.get<String>(
                    STOCK_TICKER_ARG
                )
            )
        )
    }
}
