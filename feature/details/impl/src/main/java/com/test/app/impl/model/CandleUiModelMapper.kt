package com.test.app.details.impl.model

import com.test.app.model.data.Candle

fun Candle.toUiModel() = CandleUiModel(
    open = open,
    close = close,
    high = high,
    low = low,
    timestampMs = timestampMs,
)
