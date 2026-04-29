package com.feature.details.impl.domain.mapper

import com.core.network.BuildConfig
import com.core.network.model.stocksDetails.AddressResponse
import com.core.network.model.stocksDetails.BrandingResponse
import com.core.network.model.stocksDetails.CompanyResponse
import com.core.network.model.stocksDetails.StockOverviewResponse
import com.feature.details.impl.domain.model.Address
import com.feature.details.impl.domain.model.Branding
import com.feature.details.impl.domain.model.Company
import com.feature.details.impl.domain.model.StockOverview

fun StockOverviewResponse.toDomain() = StockOverview(
    requestId = requestId,
    results = results.toDomain(),
    status = status,
)

fun CompanyResponse.toDomain() = Company(
    ticker = ticker,
    name = name,
    market = market,
    locale = locale,
    primaryExchange = primaryExchange,
    type = type,
    active = active,
    currencyName = currencyName,
    cik = cik,
    compositeFigi = compositeFigi,
    shareClassFigi = shareClassFigi,
    marketCap = marketCap,
    phoneNumber = phoneNumber,
    address = address?.toDomain(),
    description = description,
    sicCode = sicCode,
    sicDescription = sicDescription,
    tickerRoot = tickerRoot,
    homepageUrl = homepageUrl,
    totalEmployees = totalEmployees,
    listDate = listDate,
    branding = branding?.toDomain(),
    shareClassSharesOutstanding = shareClassSharesOutstanding,
    weightedSharesOutstanding = weightedSharesOutstanding,
    roundLot = roundLot,
)

fun AddressResponse.toDomain() = Address(
    address1 = address1,
    city = city,
    state = state,
    postalCode = postalCode,
)

fun BrandingResponse.toDomain() = Branding(
    logoUrl = logoUrl?.let { "$it?apiKey=${BuildConfig.API_KEY}" },
    iconUrl = iconUrl?.let { "$it?apiKey=${BuildConfig.API_KEY}" },
)
