package com.test.app.data.di

import com.test.app.data.repository.CodeChallengesRepository
import com.test.app.data.repository.CodeChallengesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface CodeChallengesRepositoryModule {
    @Binds
    fun bindsCodeChallengesRepository(
        codeChallengesRepository: CodeChallengesRepositoryImpl,
    ): CodeChallengesRepository
}
