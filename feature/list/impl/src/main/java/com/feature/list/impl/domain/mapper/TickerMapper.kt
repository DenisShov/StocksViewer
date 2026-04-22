package com.feature.list.impl.domain.mapper

import com.core.network.model.stocksList.TickerResponse
import com.core.network.model.stocksList.TickersResponse
import com.feature.list.impl.domain.model.Ticker
import com.feature.list.impl.domain.model.Tickers

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
