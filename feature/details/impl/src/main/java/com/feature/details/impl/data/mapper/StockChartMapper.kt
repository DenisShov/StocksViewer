package com.feature.details.impl.data.mapper

import com.core.network.model.stocksDetails.CandleResponse
import com.core.network.model.stocksDetails.StockChartResponse
import com.feature.details.impl.domain.model.Candle
import com.feature.details.impl.domain.model.StockChart

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
