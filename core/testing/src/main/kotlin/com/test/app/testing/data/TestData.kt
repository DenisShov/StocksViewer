package com.test.app.testing.data

import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import com.test.app.model.data.ApprovedBy
import com.test.app.model.data.Candle
import com.test.app.model.data.Company
import com.test.app.model.data.StockDetails
import com.test.app.model.data.StockOverview
import com.test.app.model.data.CreatedBy
import com.test.app.model.data.Rank
import com.test.app.model.data.StockChart
import com.test.app.model.data.Ticker
import com.test.app.network.model.NetworkApprovedBy
import com.test.app.network.model.NetworkCodeChallengeDetail
import com.test.app.network.model.NetworkCodeChallengeOverview
import com.test.app.network.model.NetworkCodeChallenges
import com.test.app.network.model.NetworkCreatedBy
import com.test.app.network.model.NetworkRank
import kotlinx.coroutines.flow.flow
import kotlin.String

val testNetworkCodeChallengeOverviewList = listOf(
    NetworkCodeChallengeOverview(
        name = "Multiples of 3 and 5",
        completedAt = "2017-04-06",
        completedLanguages = listOf(
            "javascript",
            "coffeescript",
            "ruby",
            "javascript",
            "ruby",
            "javascript",
            "ruby",
            "coffeescript",
            "javascript",
            "ruby",
            "coffeescript"
        )
    )
)

val testNetworkCodeChallenges = NetworkCodeChallenges(
    totalPages = 10,
    totalItems = 10,
    data = testNetworkCodeChallengeOverviewList
)

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

val testNetworkCodeChallengeDetail = NetworkCodeChallengeDetail(
    "id",
    "Range Extraction",
    "",
    "",
    "algorithms",
    "Write a function called `validBraces` that takes a string ...",
    listOf("Algorithms", "Validation", "Logic", "Utilities"),
    listOf("javascript", "coffeescript"),
    NetworkRank(name = "4 kyu"),
    NetworkCreatedBy(username = "username"),
    NetworkApprovedBy(username = "username"),
    100,
    55,
    40,
    20,
    "2013-11-05",
    "2013-12-05"
)

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
