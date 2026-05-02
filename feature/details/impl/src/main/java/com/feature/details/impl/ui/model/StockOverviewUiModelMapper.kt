package com.feature.details.impl.ui.model

import com.feature.details.impl.domain.model.Address
import com.feature.details.impl.domain.model.StockOverview
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun StockOverview.toUiModel() =
    StockOverviewUiModel(
        ticker = results.ticker,
        name = results.name,
        locale = results.locale,
        type = getType(results.type),
        address = getAddress(results.address),
        exchange = results.primaryExchange,
        currencyName = results.currencyName,
        marketCap = results.marketCap?.let { formatMarketCap(it) },
        description = results.description,
        homepageUrl = results.homepageUrl,
        totalEmployees = results.totalEmployees,
        sicDescription = results.sicDescription,
        cik = results.cik,
        listDate = formatDate(results.listDate),
        iconUrl = results.branding?.iconUrl,
    )

private fun getType(type: String): String = when (type) {
    "CS" -> "Common Stock"
    "ETF" -> "Exchange Traded Fund"
    "ADRC" -> "Depositary Receipt"
    else -> type
}

private fun getAddress(address: Address?) = address?.run {
    listOfNotNull(
        address1,
        city,
        state,
        postalCode,
    ).joinToString(", ")
} ?: ""

private fun formatDate(dateString: String?): String? {
    return dateString?.let {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val outputFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.getDefault())
        val date = LocalDate.parse(dateString, inputFormatter)
        date.format(outputFormatter)
    }
}

private fun formatMarketCap(marketCap: Double): String {
    return when {
        marketCap >= 1_000_000_000_000 -> "%.2fT".format(marketCap / 1_000_000_000_000)
        marketCap >= 1_000_000_000 -> "%.2fB".format(marketCap / 1_000_000_000)
        marketCap >= 1_000_000 -> "%.2fM".format(marketCap / 1_000_000)
        marketCap >= 1_000 -> "%.2fK".format(marketCap / 1_000)
        else -> "%.2f".format(marketCap)
    }
}
