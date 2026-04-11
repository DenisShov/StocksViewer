package com.core.model

data class StockOverview(
    val requestId: String,
    val results: Company,
    val status: String,
)

data class Company(
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
    val marketCap: Double? = null,
    val phoneNumber: String? = null,
    val address: Address? = null,
    val description: String? = null,
    val sicCode: String? = null,
    val sicDescription: String? = null,
    val tickerRoot: String? = null,
    val homepageUrl: String? = null,
    val totalEmployees: Long? = null,
    val listDate: String? = null,
    val branding: Branding? = null,
    val shareClassSharesOutstanding: Long? = null,
    val weightedSharesOutstanding: Long? = null,
    val roundLot: Long? = null,
)

data class Address(
    val address1: String? = null,
    val city: String? = null,
    val state: String? = null,
    val postalCode: String? = null,
)

data class Branding(
    val logoUrl: String? = null,
    val iconUrl: String? = null,
)
