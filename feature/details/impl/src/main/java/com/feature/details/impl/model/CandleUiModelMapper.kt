package com.feature.details.impl.model

import com.core.model.Candle

fun Candle.toUiModel() = CandleUiModel(
    open = open,
    close = close,
    high = high,
    low = low,
    timestampMs = timestampMs,
)
