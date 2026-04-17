package com.feature.details.impl.actions

data class StockDetailsActions(
    val onChartPeriodChange: (ChartPeriod) -> Unit = {},
    val retry: () -> Unit = {},
)
