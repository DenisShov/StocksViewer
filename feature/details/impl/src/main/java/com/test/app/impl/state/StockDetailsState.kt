package com.test.app.details.impl.state

import com.test.app.common.error.DomainError
import com.test.app.details.impl.model.CandleUiModel
import com.test.app.details.impl.model.StockOverviewUiModel

data class StockDetailsState(
    val stockOverview: StockOverviewUiModel? = null,
    val candles: List<CandleUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: DomainError? = null,
)
