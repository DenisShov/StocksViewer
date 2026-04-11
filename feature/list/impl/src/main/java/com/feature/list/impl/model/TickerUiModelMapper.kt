package com.feature.list.impl.model

import com.core.model.Ticker

fun Ticker.toUiModel() = TickerUiModel(
    ticker = ticker,
    name = name,
    type = when (type) {
        "CS" -> "Common Stock"
        "ETF" -> "Exchange Traded Fund"
        "ADRC" -> "Depositary Receipt"
        else -> type
    }
)
