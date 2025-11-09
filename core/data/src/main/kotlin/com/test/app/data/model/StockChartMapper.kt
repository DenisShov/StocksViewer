package com.test.app.data.model

import com.test.app.model.data.Candle
import com.test.app.model.data.StockChart
import com.test.app.network.model.CandleResponse
import com.test.app.network.model.StockChartResponse

fun StockChartResponse.asExternalModel() = StockChart(
    ticker = ticker,
    queryCount = queryCount,
    resultsCount = resultsCount,
    adjusted = adjusted,
    results = results.map { it.asExternalModel() },
    status = status,
    requestId = requestId,
    count = count,
)

fun CandleResponse.asExternalModel() = Candle(
    volume = volume,
    vwap = vwap,
    open = open,
    close = close,
    high = high,
    low = low,
    timestampMs = timestampMs,
    transactions = transactions,
)
