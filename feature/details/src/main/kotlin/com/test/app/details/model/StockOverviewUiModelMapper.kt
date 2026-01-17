package com.test.app.details.model

import com.test.app.model.data.StockOverview
import com.test.app.network.BuildConfig.API_KEY

fun StockOverview.toUiModel() = StockOverviewUiModel(
        ticker = results.ticker,
        name = results.name,
        locale = results.locale,
        type = results.type,
        currencyName = results.currencyName,
        marketCap = results.marketCap,
        description = results.description,
        homepageUrl = results.homepageUrl,
        totalEmployees = results.totalEmployees,
        sicDescription = results.sicDescription,
        listDate = results.listDate,
        iconUrl = "${results.branding?.iconUrl}?apiKey=$API_KEY",
    )
