package com.test.app.data.model

import com.test.app.model.data.Ticker
import com.test.app.model.data.Tickers
import com.test.app.network.model.TickerResponse
import com.test.app.network.model.TickersResponse

fun TickersResponse.toDomain() = Tickers(
    results = results.map { it.toDomain() },
    status = status,
    requestId = requestId,
    count = count,
    nextUrl = nextUrl,
)

fun TickerResponse.toDomain() = Ticker(
    ticker = ticker,
    name = name,
    market = market,
    locale = locale,
    primaryExchange = primaryExchange,
    type = type,
    active = active,
    currencyName = currencyName,
    cik = cik,
    compositeFigi = compositeFigi,
    shareClassFigi = shareClassFigi,
    lastUpdatedUtc = lastUpdatedUtc,
)
