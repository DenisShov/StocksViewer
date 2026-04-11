package com.core.data.model

import com.core.model.Candle
import com.core.model.StockChart
import com.core.network.model.CandleResponse
import com.core.network.model.StockChartResponse

fun StockChartResponse.toDomain() = StockChart(
    ticker = ticker,
    queryCount = queryCount,
    resultsCount = resultsCount,
    adjusted = adjusted,
    results = results.map { it.toDomain() },
    status = status,
    requestId = requestId,
    count = count,
)

fun CandleResponse.toDomain() = Candle(
    volume = volume,
    vwap = vwap,
    open = open,
    close = close,
    high = high,
    low = low,
    timestampMs = timestampMs,
    transactions = transactions,
)
