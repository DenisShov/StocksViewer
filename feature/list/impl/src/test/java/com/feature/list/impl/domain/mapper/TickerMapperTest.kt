package com.feature.list.impl.domain.mapper

import com.core.network.model.stocksList.TickerResponse
import com.core.network.model.stocksList.TickersResponse
import com.feature.list.impl.data.mapper.toDomain
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TickerMapperTest {

    private val tickerResponse = TickerResponse(
        ticker = "AAPL",
        name = "Apple Inc.",
        market = "stocks",
        locale = "us",
        primaryExchange = "XNAS",
        type = "CS",
        active = true,
        currencyName = "usd",
        cik = "0000320193",
        compositeFigi = "BBG000B9XRY4",
        shareClassFigi = "BBG001S5N8V8",
        lastUpdatedUtc = "2024-01-15T00:00:00Z",
    )

    @Test
    fun tickerResponse_toDomain_mapsAllFields() {
        val result = tickerResponse.toDomain()
        assertEquals("AAPL", result.ticker)
        assertEquals("Apple Inc.", result.name)
        assertEquals("stocks", result.market)
        assertEquals("us", result.locale)
        assertEquals("XNAS", result.primaryExchange)
        assertEquals("CS", result.type)
        assertEquals(true, result.active)
        assertEquals("usd", result.currencyName)
        assertEquals("0000320193", result.cik)
        assertEquals("BBG000B9XRY4", result.compositeFigi)
        assertEquals("BBG001S5N8V8", result.shareClassFigi)
        assertEquals("2024-01-15T00:00:00Z", result.lastUpdatedUtc)
    }

    @Test
    fun tickerResponse_toDomain_nullOptionalFields() {
        val response = tickerResponse.copy(
            currencyName = null,
            cik = null,
            compositeFigi = null,
            shareClassFigi = null,
            lastUpdatedUtc = null,
        )
        val result = response.toDomain()
        assertNull(result.currencyName)
        assertNull(result.cik)
        assertNull(result.compositeFigi)
        assertNull(result.shareClassFigi)
        assertNull(result.lastUpdatedUtc)
    }

    @Test
    fun tickersResponse_toDomain_mapsResultsList() {
        val response = TickersResponse(
            results = listOf(tickerResponse, tickerResponse.copy(ticker = "GOOGL")),
            status = "OK",
            requestId = "req-1",
            count = 2,
            nextUrl = "https://api.polygon.io/v3/reference/tickers?cursor=abc",
        )
        val result = response.toDomain()
        assertEquals(2, result.results.size)
        assertEquals("AAPL", result.results[0].ticker)
        assertEquals("GOOGL", result.results[1].ticker)
        assertEquals("OK", result.status)
        assertEquals("req-1", result.requestId)
        assertEquals(2, result.count)
        assertEquals("https://api.polygon.io/v3/reference/tickers?cursor=abc", result.nextUrl)
    }

    @Test
    fun tickersResponse_toDomain_emptyResults() {
        val response = TickersResponse(
            results = emptyList(),
            status = "OK",
            requestId = "req-1",
            count = 0,
        )
        val result = response.toDomain()
        assertTrue(result.results.isEmpty())
        assertEquals(0, result.count)
        assertNull(result.nextUrl)
    }
}
