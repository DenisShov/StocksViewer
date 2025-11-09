package com.test.app.model.data

class StockChart(
    val ticker: String,
    val queryCount: Int,
    val resultsCount: Int,
    val adjusted: Boolean,
    val results: List<Candle>,
    val status: String,
    val requestId: String,
    val count: Int
)

data class Candle(
    /** Volume */
    val volume: Double,
    /** Volume-weighted average price */
    val vwap: Double? = null,
    val open: Double,
    val close: Double,
    val high: Double,
    val low: Double,
    /** Timestamp in epoch millis */
    val timestampMs: Long,
    /** Number of transactions */
    val transactions: Int
)
