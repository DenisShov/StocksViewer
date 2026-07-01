package com.feature.list.impl.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class TickerUiModel(
    val ticker: String,
    val name: String,
    val type: String,
)
