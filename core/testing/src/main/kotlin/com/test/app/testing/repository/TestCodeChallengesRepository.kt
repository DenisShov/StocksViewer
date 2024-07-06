package com.test.app.testing.repository

import androidx.paging.PagingData
import com.test.app.data.repository.CodeChallengesRepository
import com.test.app.model.data.CodeChallengeDetail
import com.test.app.model.data.CodeChallengeOverview
import com.test.app.testing.data.testCodeChallengeDetail
import com.test.app.testing.data.testFlowPagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TestCodeChallengesRepository : CodeChallengesRepository {

    override fun getCodeChallengesFlow(): Flow<PagingData<CodeChallengeOverview>> {
        return testFlowPagingData
    }

    override suspend fun getCodeChallengeById(challengeId: String): CodeChallengeDetail {
        return testCodeChallengeDetail
    }
}
