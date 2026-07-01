package com.feature.details.impl.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class CandleUiModel(
    val open: Double,
    val close: Double,
    val high: Double,
    val low: Double,
    val timestampMs: Long,
)
