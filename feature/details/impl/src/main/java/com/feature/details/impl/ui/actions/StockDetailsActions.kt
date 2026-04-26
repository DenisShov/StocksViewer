package com.feature.details.impl.ui.actions

data class StockDetailsActions(
    val onChartPeriodChange: (ChartPeriod) -> Unit = {},
    val retry: () -> Unit = {},
    val retryChart: () -> Unit = {},
)
