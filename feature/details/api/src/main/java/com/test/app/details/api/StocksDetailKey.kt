package com.test.app.details.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class StocksDetailKey(val stockTicker: String) : NavKey
