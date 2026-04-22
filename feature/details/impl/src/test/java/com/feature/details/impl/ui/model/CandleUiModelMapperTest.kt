package com.feature.details.impl.ui.model

import com.feature.details.impl.domain.model.Candle
import org.junit.Test
import kotlin.test.assertEquals

class CandleUiModelMapperTest {

    private val candle = Candle(
        volume = 50_000_000.0,
        vwap = 185.5,
        open = 185.82,
        close = 184.80,
        high = 186.03,
        low = 184.21,
        timestampMs = 1699851600000,
        transactions = 500000,
    )

    @Test
    fun toUiModel_mapsOpenCloseHighLow() {
        val result = candle.toUiModel()
        assertEquals(185.82, result.open)
        assertEquals(184.80, result.close)
        assertEquals(186.03, result.high)
        assertEquals(184.21, result.low)
    }

    @Test
    fun toUiModel_mapsTimestamp() {
        val result = candle.toUiModel()
        assertEquals(1699851600000, result.timestampMs)
    }
}
