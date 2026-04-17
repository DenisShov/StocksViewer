package com.feature.details.impl.state

import com.feature.details.impl.actions.ChartPeriod
import com.feature.details.impl.model.CandleUiModel
import com.feature.details.impl.model.StockOverviewUiModel

data class StockDetailsState(
    val stockOverview: StockOverviewUiModel? = null,
    val candles: List<CandleUiModel> = emptyList(),
    val selectedPeriod: ChartPeriod = ChartPeriod.WEEK,
    val isLoading: Boolean = false,
    val errorString: String? = null,
)
