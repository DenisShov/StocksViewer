package com.test.app.details.state

import com.test.app.common.error.DomainError
import com.test.app.details.model.CandleUiModel
import com.test.app.details.model.StockOverviewUiModel

data class StockDetailsState(
    val stockOverview: StockOverviewUiModel? = null,
    val candles: List<CandleUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: DomainError? = null,
)
