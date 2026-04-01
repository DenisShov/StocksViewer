package com.test.app.details.actions

data class StockDetailsActions(
    val onChartPeriodChange: (ChartPeriod) -> Unit = {},
    val retry: () -> Unit = {},
)
