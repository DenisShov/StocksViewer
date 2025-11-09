package com.test.app.network.model

import com.google.gson.annotations.SerializedName

data class TickersResponse(
    val results: List<TickerResponse>,
    val status: String,
    @SerializedName("request_id")
    val requestId: String,
    val count: Int,
    @SerializedName("next_url")
    val nextUrl: String? = null,
)

data class TickerResponse(
    val ticker: String,
    val name: String,
    val market: String,
    val locale: String,
    @SerializedName("primary_exchange")
    val primaryExchange: String,
    val type: String,
    val active: Boolean,
    @SerializedName("currency_name")
    val currencyName: String? = null,
    val cik: String? = null,
    @SerializedName("composite_figi")
    val compositeFigi: String? = null,
    @SerializedName("share_class_figi")
    val shareClassFigi: String? = null,
    @SerializedName("last_updated_utc")
    val lastUpdatedUtc: String? = null,
)
