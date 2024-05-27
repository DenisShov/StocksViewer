package com.test.app.data.repository

import androidx.paging.Pager
import com.test.app.data.model.asExternalModel
import com.test.app.model.data.CodeChallengeOverview
import com.test.app.network.retrofit.CodeWarsApi
import javax.inject.Inject

class CodeChallengesRepositoryImpl @Inject constructor(
    private val codeWarsApi: CodeWarsApi, private val pager: Pager<Int, CodeChallengeOverview>
) : CodeChallengesRepository {

    override fun getCodeChallengesFlow() = pager.flow

    override suspend fun getCodeChallengeById(challengeId: String) =
        codeWarsApi.getCodeChallenge(challengeId).asExternalModel()

}
