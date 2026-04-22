package com.feature.details.impl.ui.model

data class CandleUiModel(
    val open: Double,
    val close: Double,
    val high: Double,
    val low: Double,
    val timestampMs: Long,
)
