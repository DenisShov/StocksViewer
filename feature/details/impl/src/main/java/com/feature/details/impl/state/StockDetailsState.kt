package com.feature.details.impl.state

import com.core.common.error.DomainError
import com.feature.details.impl.model.CandleUiModel
import com.feature.details.impl.model.StockOverviewUiModel

data class StockDetailsState(
    val stockOverview: StockOverviewUiModel? = null,
    val candles: List<CandleUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: DomainError? = null,
)