package com.test.app.testing.data

import androidx.paging.PagingData
import com.test.app.model.data.ApprovedBy
import com.test.app.model.data.CodeChallengeDetail
import com.test.app.model.data.CodeChallengeOverview
import com.test.app.model.data.CreatedBy
import com.test.app.model.data.Rank
import com.test.app.network.model.NetworkApprovedBy
import com.test.app.network.model.NetworkCodeChallengeDetail
import com.test.app.network.model.NetworkCreatedBy
import com.test.app.network.model.NetworkRank
import kotlinx.coroutines.flow.flow

val testCodeChallengeOverviewList = listOf(
    CodeChallengeOverview(
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

val testPagingData = PagingData.from(testCodeChallengeOverviewList)

val testFlowPagingData = flow {
    emit(testPagingData)
}

val testCodeChallengeDetail = CodeChallengeDetail(
    "id",
    "Range Extraction",
    "",
    "",
    "algorithms",
    "Write a function called `validBraces` that takes a string ...",
    listOf("Algorithms", "Validation", "Logic", "Utilities"),
    listOf("javascript", "coffeescript"),
    Rank(name = "4 kyu"),
    CreatedBy(username = "username"),
    ApprovedBy(username = "username"),
    100,
    100,
    50,
    50,
    "2013-11-05",
    "2013-11-05"
)

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
    100,
    50,
    50,
    "2013-11-05",
    "2013-11-05"
)
