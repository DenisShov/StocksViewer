package com.test.app.data.model

import com.test.app.model.data.Ticker
import com.test.app.network.model.TickerResponse

fun TickerResponse.asExternalModel() = Ticker(
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
