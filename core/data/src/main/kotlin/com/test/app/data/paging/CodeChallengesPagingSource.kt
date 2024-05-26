package com.test.app.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.test.app.data.model.asExternalModel
import com.test.app.model.data.CodeChallengeOverview
import com.test.app.network.retrofit.CodeWarsApi
import javax.inject.Inject

class CodeChallengesPagingSource @Inject constructor(
    private val codeWarsApi: CodeWarsApi
) : PagingSource<Int, CodeChallengeOverview>() {

    private var currentKey: Int = 0

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, CodeChallengeOverview> {
        try {
            val nextPageNumber = params.key ?: 0
            val response = codeWarsApi.getCompletedCodeChallenges(nextPageNumber)
            val nextKey = if (currentKey < response.totalPages) currentKey++ else null

            return LoadResult.Page(
                data = response.data.map { it.asExternalModel() },
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CodeChallengeOverview>): Int? {
        // Try to find the page key of the closest page to anchorPosition from
        // either the prevKey or the nextKey; you need to handle nullability
        // here.
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey are null -> anchorPage is the
        //    initial page, so return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
