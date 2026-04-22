package com.core.testing.data

import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import com.core.model.Candle
import com.core.model.Company
import com.core.model.StockChart
import com.core.model.StockOverview
import com.core.model.Ticker
import com.core.model.Tickers
import com.core.network.model.stocksDetails.CandleResponse
import com.core.network.model.stocksDetails.CompanyResponse
import com.core.network.model.stocksDetails.StockChartResponse
import com.core.network.model.stocksDetails.StockOverviewResponse
import com.core.network.model.stocksList.TickerResponse
import com.core.network.model.stocksList.TickersResponse
import kotlinx.coroutines.flow.flow

const val TEST_TICKER = "AAPL"
const val TEST_COMPANY_NAME = "Apple Inc."
const val TEST_MARKET = "stocks"
const val TEST_LOCALE = "us"
const val TEST_EXCHANGE = "XNAS"
const val TEST_TYPE = "CS"

val testStockOverviewLists = listOf(
    Ticker(
        ticker = TEST_TICKER,
        name = TEST_COMPANY_NAME,
        market = TEST_MARKET,
        locale = TEST_LOCALE,
        primaryExchange = TEST_EXCHANGE,
        type = TEST_TYPE,
        active = true,
        currencyName = "usd",
        cik = null,
        compositeFigi = null,
        shareClassFigi = null,
        lastUpdatedUtc = null,
    ),
)

val testTickers = Tickers(
    results = testStockOverviewLists,
    status = "OK",
    requestId = "test-request-id",
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
    ),
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
    ),
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
    ),
)

val testFlowPagingDataAppendError = flow {
    emit(testPagingDataAppendError)
}

val testStockDetails = StockOverview(
    requestId = "test-request-id",
    results = Company(
        ticker = TEST_TICKER,
        name = TEST_COMPANY_NAME,
        market = TEST_MARKET,
        locale = TEST_LOCALE,
        primaryExchange = TEST_EXCHANGE,
        type = TEST_TYPE,
        active = true,
        currencyName = "usd",
        marketCap = 3_000_000_000_000.0,
        totalEmployees = 164000,
        description = "Apple Inc. designs, manufactures, and markets smartphones and personal computers.",
        sicDescription = "Electronic Computers",
    ),
    status = "OK",
)

val testCandles = listOf(
    Candle(
        volume = 50_000_000.0,
        vwap = 185.5,
        open = 185.82,
        close = 184.8,
        high = 186.03,
        low = 184.21,
        timestampMs = 1699851600000,
        transactions = 500000,
    ),
    Candle(
        volume = 45_000_000.0,
        vwap = 187.0,
        open = 187.7,
        close = 187.44,
        high = 188.11,
        low = 186.3,
        timestampMs = 1699938000000,
        transactions = 450000,
    ),
)

val testStockChart = StockChart(
    ticker = TEST_TICKER,
    queryCount = 2,
    resultsCount = 2,
    adjusted = true,
    results = testCandles,
    status = "OK",
    requestId = "test-request-id",
    count = 2,
)

val tickerResponse = TickerResponse(
    ticker = "AAPL",
    name = "Apple Inc.",
    market = "stocks",
    locale = "us",
    primaryExchange = "XNAS",
    type = "CS",
    active = true,
)

val tickersResponse = TickersResponse(
    results = listOf(tickerResponse),
    status = "OK",
    requestId = "req1",
    count = 1,
)

val companyResponse = CompanyResponse(
    ticker = "AAPL",
    name = "Apple Inc.",
    market = "stocks",
    locale = "us",
    primaryExchange = "XNAS",
    type = "CS",
    active = true,
)

val stockOverviewResponse = StockOverviewResponse(
    requestId = "req1",
    results = companyResponse,
    status = "OK",
)

val candleResponse = CandleResponse(
    volume = 1000.0,
    vwap = 149.5,
    open = 148.0,
    close = 150.0,
    high = 151.0,
    low = 147.0,
    timestampMs = 1704067200000,
    transactions = 500,
)

val stockChartResponse = StockChartResponse(
    ticker = "AAPL",
    queryCount = 1,
    resultsCount = 1,
    adjusted = true,
    results = listOf(candleResponse),
    status = "OK",
    requestId = "req1",
    count = 1,
)
