package com.feature.list.impl.domain.model

data class Tickers(
    val results: List<Ticker>,
    val status: String,
    val requestId: String,
    val count: Int,
    val nextUrl: String? = null,
)
