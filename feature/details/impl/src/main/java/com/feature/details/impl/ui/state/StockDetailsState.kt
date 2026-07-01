package com.feature.details.impl.ui.state

import com.feature.details.impl.ui.actions.ChartPeriod
import com.feature.details.impl.ui.model.CandleUiModel
import com.feature.details.impl.ui.model.StockOverviewUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class StockDetailsState(
    val stockOverview: StockOverviewUiModel? = null,
    val candles: ImmutableList<CandleUiModel> = persistentListOf(),
    val selectedPeriod: ChartPeriod = ChartPeriod.WEEK,
    val isLoading: Boolean = false,
    val errorString: String? = null,
    val isChartLoading: Boolean = false,
    val chartErrorString: String? = null,
    val isFavorite: Boolean = false,
)
