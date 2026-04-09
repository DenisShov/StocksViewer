package com.test.app.data.model

import com.test.app.model.data.Address
import com.test.app.model.data.Branding
import com.test.app.model.data.Company
import com.test.app.model.data.StockOverview
import com.test.app.network.model.AddressResponse
import com.test.app.network.model.BrandingResponse
import com.test.app.network.model.CompanyResponse
import com.test.app.network.model.StockOverviewResponse
import com.test.app.stockviewer.core.network.BuildConfig

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
