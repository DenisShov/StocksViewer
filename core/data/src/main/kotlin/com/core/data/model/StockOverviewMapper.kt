package com.core.data.model

import com.core.model.Address
import com.core.model.Branding
import com.core.model.Company
import com.core.model.StockOverview
import com.core.network.BuildConfig
import com.core.network.model.AddressResponse
import com.core.network.model.BrandingResponse
import com.core.network.model.CompanyResponse
import com.core.network.model.StockOverviewResponse

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
