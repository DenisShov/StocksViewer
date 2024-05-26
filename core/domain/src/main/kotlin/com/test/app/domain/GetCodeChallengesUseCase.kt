package com.test.app.domain

import com.test.app.data.repository.CodeChallengesRepository
import javax.inject.Inject

class GetCodeChallengesUseCase @Inject constructor(
    private val codeChallengesRepository: CodeChallengesRepository
) {

    fun launch() = codeChallengesRepository.getCodeChallengesFlow()
}
