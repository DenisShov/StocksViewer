package com.feature.list.impl.ui.model

import com.feature.list.impl.domain.model.Ticker

fun Ticker.toUiModel() = TickerUiModel(
    ticker = ticker,
    name = name,
    type = getType()
)

private fun Ticker.getType(): String = when (type) {
    "CS" -> "Common Stock"
    "ETF" -> "Exchange Traded Fund"
    "ADRC" -> "Depositary Receipt"
    else -> type
}
