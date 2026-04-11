package com.core.data.model

import com.core.model.Ticker
import com.core.model.Tickers
import com.core.network.model.TickerResponse
import com.core.network.model.TickersResponse

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
