package com.feature.details.impl.domain.mapper

import com.core.network.model.stocksDetails.CandleResponse
import com.core.network.model.stocksDetails.StockChartResponse
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class StockChartMapperTest {

    private val candleResponse = CandleResponse(
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
    fun candleResponse_toDomain_mapsAllFields() {
        val result = candleResponse.toDomain()
        assertEquals(50_000_000.0, result.volume)
        assertEquals(185.5, result.vwap)
        assertEquals(185.82, result.open)
        assertEquals(184.80, result.close)
        assertEquals(186.03, result.high)
        assertEquals(184.21, result.low)
        assertEquals(1699851600000, result.timestampMs)
        assertEquals(500000, result.transactions)
    }

    @Test
    fun candleResponse_toDomain_nullVwap() {
        val response = candleResponse.copy(vwap = null)
        val result = response.toDomain()
        assertNull(result.vwap)
    }

    @Test
    fun stockChartResponse_toDomain_mapsAllFields() {
        val response = StockChartResponse(
            ticker = "AAPL",
            queryCount = 2,
            resultsCount = 2,
            adjusted = true,
            results = listOf(candleResponse, candleResponse.copy(open = 187.7)),
            status = "OK",
            requestId = "req-1",
            count = 2,
        )
        val result = response.toDomain()
        assertEquals("AAPL", result.ticker)
        assertEquals(2, result.queryCount)
        assertEquals(2, result.resultsCount)
        assertEquals(true, result.adjusted)
        assertEquals(2, result.results.size)
        assertEquals("OK", result.status)
        assertEquals("req-1", result.requestId)
        assertEquals(2, result.count)
    }

    @Test
    fun stockChartResponse_toDomain_mapsCandles() {
        val response = StockChartResponse(
            ticker = "AAPL",
            queryCount = 1,
            resultsCount = 1,
            adjusted = true,
            results = listOf(candleResponse),
            status = "OK",
            requestId = "req-1",
            count = 1,
        )
        val result = response.toDomain()
        val candle = result.results.first()
        assertEquals(185.82, candle.open)
        assertEquals(184.80, candle.close)
        assertEquals(186.03, candle.high)
        assertEquals(184.21, candle.low)
    }

    @Test
    fun stockChartResponse_toDomain_emptyResults() {
        val response = StockChartResponse(
            ticker = "AAPL",
            queryCount = 0,
            resultsCount = 0,
            adjusted = true,
            results = emptyList(),
            status = "OK",
            requestId = "req-1",
            count = 0,
        )
        val result = response.toDomain()
        assertTrue(result.results.isEmpty())
    }
}
