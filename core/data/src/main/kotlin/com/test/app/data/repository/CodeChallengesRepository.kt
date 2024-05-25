package com.test.app.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.test.app.data.paging.CodeChallengesPagingSource
import com.test.app.network.retrofit.CodeWarsApi
import javax.inject.Inject

class CodeChallengesRepository @Inject constructor(private val codeWarsApi: CodeWarsApi) {

    fun getCodeChallengesFlow() = Pager(
        PagingConfig(pageSize = 2)
    ) {
        CodeChallengesPagingSource(codeWarsApi)
    }.flow

    suspend fun getCodeChallengeById(challengeId: String) = codeWarsApi.getCodeChallenge(challengeId)

}
