package com.test.app.details.actions

data class StockDetailsActions(
    val onChartPeriodChange: (String) -> Unit = {},
)
