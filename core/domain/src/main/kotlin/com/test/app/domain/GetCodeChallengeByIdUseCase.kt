package com.test.app.domain

import com.test.app.common.dispatcher.DispatcherProvider
import com.test.app.common.result.DataResult
import com.test.app.common.result.asDataResult
import com.test.app.data.repository.CodeChallengesRepository
import com.test.app.model.data.CodeChallengeDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetCodeChallengeByIdUseCase @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val codeChallengesRepository: CodeChallengesRepository,
) {
    suspend fun launch(challengeId: String): Flow<DataResult<CodeChallengeDetail>> {
        return flow {
            emit(codeChallengesRepository.getCodeChallengeById(challengeId))
        }
            .asDataResult()
            .flowOn(dispatcherProvider.io)
    }
}
