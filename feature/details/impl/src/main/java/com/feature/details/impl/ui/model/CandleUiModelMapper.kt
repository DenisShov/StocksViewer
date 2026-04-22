package com.feature.details.impl.ui.model

import com.feature.details.impl.domain.model.Candle

fun Candle.toUiModel() = CandleUiModel(
    open = open,
    close = close,
    high = high,
    low = low,
    timestampMs = timestampMs,
)
