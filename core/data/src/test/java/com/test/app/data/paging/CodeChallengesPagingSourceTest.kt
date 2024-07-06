package com.test.app.data.paging

import androidx.paging.PagingSource
import com.test.app.data.utils.DATE_TIME_PATTERN
import com.test.app.model.data.CodeChallengeOverview
import com.test.app.network.retrofit.CodeWarsApi
import com.test.app.testing.data.testCodeChallengeOverviewList
import com.test.app.testing.data.testNetworkCodeChallengeOverviewList
import com.test.app.testing.data.testNetworkCodeChallenges
import com.test.app.testing.utils.BaseCoroutineTestWithTestDispatcherProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.joda.time.DateTime
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CodeChallengesPagingSourceTest : BaseCoroutineTestWithTestDispatcherProvider() {

    private lateinit var codeChallengesPagingSource: CodeChallengesPagingSource

    @MockK
    private lateinit var getCodeWarsApi: CodeWarsApi

    @MockK
    private lateinit var dateTime: DateTime

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        codeChallengesPagingSource = CodeChallengesPagingSource(codeWarsApi = getCodeWarsApi)
    }

    @Test
    fun `test CodeChallengesPagingSource Refresh returns success result`() = runTest {
        mockDateTime(testNetworkCodeChallengeOverviewList[0].completedAt)

        val key = 0

        coEvery { getCodeWarsApi.getCompletedCodeChallenges(key) } returns testNetworkCodeChallenges

        val params = PagingSource.LoadParams.Refresh(
            key = key,
            loadSize = 1,
            placeholdersEnabled = false
        )

        val expected = PagingSource.LoadResult.Page(
            data = testCodeChallengeOverviewList,
            prevKey = null,
            nextKey = 1
        )

        val actual = codeChallengesPagingSource.load(params = params)

        assertEquals(expected, actual)
    }

    @Test
    fun `test CodeChallengesPagingSource Refresh returns error result`() = runTest {
        val key = 0

        val error = RuntimeException("some error")

        coEvery { getCodeWarsApi.getCompletedCodeChallenges(key) } throws error

        val params = PagingSource.LoadParams.Refresh(
            key = key,
            loadSize = 1,
            placeholdersEnabled = false
        )

        val expected = PagingSource.LoadResult.Error<Int, CodeChallengeOverview>(
            throwable = error
        )

        val actual = codeChallengesPagingSource.load(params = params)

        assertEquals(expected, actual)
    }

    @Test
    fun `test CodeChallengesPagingSource Append end of list reached`() = runTest {
        mockDateTime(testNetworkCodeChallengeOverviewList[0].completedAt)

        val key = 10

        coEvery { getCodeWarsApi.getCompletedCodeChallenges(key) } returns testNetworkCodeChallenges

        val params = PagingSource.LoadParams.Append(
            key = key,
            loadSize = 1,
            placeholdersEnabled = false
        )

        val expected = PagingSource.LoadResult.Page(
            data = testCodeChallengeOverviewList,
            prevKey = null,
            nextKey = null
        )

        val actual = codeChallengesPagingSource.load(params = params)

        assertEquals(expected, actual)
    }

    private fun mockDateTime(date: String) {
        mockkStatic(DateTime::class)
        every { DateTime.parse(any()) } returns dateTime
        every { dateTime.toString(DATE_TIME_PATTERN) } returns date
    }

}