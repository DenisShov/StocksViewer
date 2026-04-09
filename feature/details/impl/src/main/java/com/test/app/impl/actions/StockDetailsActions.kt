package com.test.app.details.impl.actions

data class StockDetailsActions(
    val onChartPeriodChange: (ChartPeriod) -> Unit = {},
    val retry: () -> Unit = {},
)
