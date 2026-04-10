package com.test.app.testing.data

import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import com.test.app.model.data.Company
import com.test.app.model.data.StockChart
import com.test.app.model.data.StockOverview
import com.test.app.model.data.Ticker
import com.test.app.model.data.Tickers
import kotlinx.coroutines.flow.flow

val testStockOverviewLists = listOf(
    Ticker(
        ticker = "", // TODO: add test data
        name = "",
        market = "",
        locale = "",
        primaryExchange = "",
        type = "",
        active = true,
        currencyName = null,
        cik = null,
        compositeFigi = null,
        shareClassFigi = null,
        lastUpdatedUtc = null,
    )
)

val testTickers = Tickers(
    results = testStockOverviewLists,
    status = "status",
    requestId = "requestId",
    count = 1,
    nextUrl = null,
)

val testPagingData = PagingData.from(testStockOverviewLists)

val testFlowPagingData = flow {
    emit(testPagingData)
}

val testPagingDataNotLoading = PagingData.from(
    data = testStockOverviewLists,
    sourceLoadStates = LoadStates(
        refresh = LoadState.NotLoading(false),
        append = LoadState.NotLoading(false),
        prepend = LoadState.NotLoading(false),
    )
)

val testFlowPagingDataNotLoading = flow {
    emit(testPagingDataNotLoading)
}

val testPagingDataAppendLoading = PagingData.from(
    data = testStockOverviewLists,
    sourceLoadStates = LoadStates(
        refresh = LoadState.NotLoading(false),
        append = LoadState.Loading,
        prepend = LoadState.NotLoading(false),
    )
)

val testFlowPagingDataAppendLoading = flow {
    emit(testPagingDataAppendLoading)
}

const val testErrorMessage = "Test error"

val testPagingDataAppendError = PagingData.from(
    data = testStockOverviewLists,
    sourceLoadStates = LoadStates(
        refresh = LoadState.NotLoading(false),
        append = LoadState.Error(RuntimeException(testErrorMessage)),
        prepend = LoadState.NotLoading(false),
    )
)

val testFlowPagingDataAppendError = flow {
    emit(testPagingDataAppendError)
}

val testStockDetails = StockOverview(
    requestId = "", // TODO: add test data
    results = Company(
        ticker = "",
        name = "",
        market = "",
        locale = "",
        primaryExchange = "",
        type = "",
        active = true,
    ),
    status = "",
)

val testStockChart = StockChart(
    ticker = "", // TODO: add test data
    queryCount = 0,
    resultsCount = 0,
    adjusted = true,
    results = emptyList(),
    status = "",
    requestId = "",
    count = 0,
)
