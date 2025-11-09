package com.test.app.model.data

data class Ticker(
    val ticker: String,
    val name: String,
    val market: String,
    val locale: String,
    val primaryExchange: String,
    val type: String,
    val active: Boolean,
    val currencyName: String? = null,
    val cik: String? = null,
    val compositeFigi: String? = null,
    val shareClassFigi: String? = null,
    val lastUpdatedUtc: String? = null,
)