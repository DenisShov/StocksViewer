package com.core.network.model.stocksDetails

import com.google.gson.annotations.SerializedName

class StockChartResponse(
    val ticker: String,
    val queryCount: Int,
    val resultsCount: Int,
    val adjusted: Boolean,
    val results: List<CandleResponse>,
    val status: String,
    @SerializedName("request_id")
    val requestId: String,
    val count: Int,
)

data class CandleResponse(
    /** Volume */
    @SerializedName("v")
    val volume: Double,
    /** Volume-weighted average price */
    @SerializedName("vw")
    val vwap: Double? = null,
    @SerializedName("o")
    val open: Double,
    @SerializedName("c")
    val close: Double,
    @SerializedName("h")
    val high: Double,
    @SerializedName("l")
    val low: Double,
    /** Timestamp in epoch millis */
    @SerializedName("t")
    val timestampMs: Long,
    /** Number of transactions */
    @SerializedName("n")
    val transactions: Int,
)
