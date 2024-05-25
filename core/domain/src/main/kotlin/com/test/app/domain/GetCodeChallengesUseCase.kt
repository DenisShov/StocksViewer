package com.test.app.domain

import com.test.app.common.dispatcher.DispatcherProvider
import com.test.app.common.result.asDataResult
import com.test.app.data.repository.CodeChallengesRepository
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetCodeChallengesUseCase @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val codeChallengesRepository: CodeChallengesRepository,
) {

    fun launch() = codeChallengesRepository.getCodeChallengesFlow()
        .asDataResult()
        .flowOn(dispatcherProvider.io)
}
