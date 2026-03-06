package com.test.app.model.data

data class Tickers(
    val results: List<Ticker>,
    val status: String,
    val requestId: String,
    val count: Int,
    val nextUrl: String? = null,
)
