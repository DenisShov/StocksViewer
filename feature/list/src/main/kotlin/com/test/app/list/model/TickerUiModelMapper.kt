package com.test.app.list.model

import com.test.app.model.data.Ticker

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
