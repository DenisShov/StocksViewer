package com.test.app.data.model

import com.test.app.model.data.Address
import com.test.app.model.data.Branding
import com.test.app.model.data.Company
import com.test.app.model.data.StockOverview
import com.test.app.network.model.AddressResponse
import com.test.app.network.model.BrandingResponse
import com.test.app.network.model.CompanyResponse
import com.test.app.network.model.StockOverviewResponse

fun StockOverviewResponse.asExternalModel() = StockOverview(
    requestId = requestId,
    results = results.asExternalModel(),
    status = status,
)

fun CompanyResponse.asExternalModel() = Company(
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
    address = address?.asExternalModel(),
    description = description,
    sicCode = sicCode,
    sicDescription = sicDescription,
    tickerRoot = tickerRoot,
    homepageUrl = homepageUrl,
    totalEmployees = totalEmployees,
    listDate = listDate,
    branding = branding?.asExternalModel(),
    shareClassSharesOutstanding = shareClassSharesOutstanding,
    weightedSharesOutstanding = weightedSharesOutstanding,
    roundLot = roundLot,
)

fun AddressResponse.asExternalModel() = Address(
    address1 = address1,
    city = city,
    state = state,
    postalCode = postalCode,
)

fun BrandingResponse.asExternalModel() = Branding(
    logoUrl = logoUrl,
    iconUrl = iconUrl,
)
