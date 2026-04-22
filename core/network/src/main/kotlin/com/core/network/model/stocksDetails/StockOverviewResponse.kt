package com.core.network.model.stocksDetails

import com.google.gson.annotations.SerializedName

data class StockOverviewResponse(
    @SerializedName("request_id")
    val requestId: String,
    val results: CompanyResponse,
    val status: String,
)

data class CompanyResponse(
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
    @SerializedName("market_cap")
    val marketCap: Double? = null,
    @SerializedName("phone_number")
    val phoneNumber: String? = null,
    val address: AddressResponse? = null,
    val description: String? = null,
    @SerializedName("sic_code")
    val sicCode: String? = null,
    @SerializedName("sic_description")
    val sicDescription: String? = null,
    @SerializedName("ticker_root")
    val tickerRoot: String? = null,
    @SerializedName("homepage_url")
    val homepageUrl: String? = null,
    @SerializedName("total_employees")
    val totalEmployees: Long? = null,
    @SerializedName("list_date")
    val listDate: String? = null,
    val branding: BrandingResponse? = null,
    @SerializedName("share_class_shares_outstanding")
    val shareClassSharesOutstanding: Long? = null,
    @SerializedName("weighted_shares_outstanding")
    val weightedSharesOutstanding: Long? = null,
    @SerializedName("round_lot")
    val roundLot: Long? = null,
)

data class AddressResponse(
    @SerializedName("address1")
    val address1: String? = null,
    val city: String? = null,
    val state: String? = null,
    @SerializedName("postal_code")
    val postalCode: String? = null,
)

data class BrandingResponse(
    @SerializedName("logo_url")
    val logoUrl: String? = null,
    @SerializedName("icon_url")
    val iconUrl: String? = null,
)
