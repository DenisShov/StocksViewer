package com.test.app.data.repository

import androidx.paging.PagingData
import com.test.app.model.data.CodeChallengeDetail
import com.test.app.model.data.CodeChallengeOverview
import kotlinx.coroutines.flow.Flow

interface CodeChallengesRepository {

    fun getCodeChallengesFlow(): Flow<PagingData<CodeChallengeOverview>>

    suspend fun getCodeChallengeById(challengeId: String): CodeChallengeDetail

}
