package com.test.app.details.model

import com.test.app.model.data.Address
import com.test.app.model.data.StockOverview
import com.test.app.network.BuildConfig.API_KEY
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun StockOverview.toUiModel() = StockOverviewUiModel(
    ticker = results.ticker,
    name = results.name,
    locale = results.locale,
    type = results.type,
    address = getAddress(results.address),
    exchange = results.primaryExchange,
    currencyName = results.currencyName,
    marketCap = results.marketCap,
    description = results.description,
    homepageUrl = results.homepageUrl,
    totalEmployees = results.totalEmployees,
    sicDescription = results.sicDescription,
    cik = results.cik,
    listDate = formatDate(results.listDate),
    iconUrl = "${results.branding?.iconUrl}?apiKey=$API_KEY",
)

private fun getAddress(address: Address?) = address?.run {
    listOfNotNull(
        address1,
        city,
        state,
        postalCode,
    ).joinToString(", ")
} ?: ""


fun formatDate(dateString: String?): String? {
    return dateString?.let {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val outputFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.getDefault())
        val date = LocalDate.parse(dateString, inputFormatter)
        date.format(outputFormatter)
    }
}
