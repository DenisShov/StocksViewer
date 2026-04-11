package com.feature.details.impl.model

data class StockOverviewUiModel(
    val ticker: String = "",
    val name: String = "",
    val locale: String = "",
    val type: String = "",
    val exchange: String = "",
    val address: String? = null,
    val currencyName: String? = null,
    val marketCap: String? = null,
    val description: String? = null,
    val homepageUrl: String? = null,
    val totalEmployees: Long? = null,
    val sicDescription: String? = null,
    val cik: String? = null,
    val listDate: String? = null,
    val iconUrl: String? = null,
)

