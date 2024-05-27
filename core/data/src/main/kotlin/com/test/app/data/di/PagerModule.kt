package com.test.app.data.di

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.test.app.data.paging.CodeChallengesPagingSource
import com.test.app.model.data.CodeChallengeOverview
import com.test.app.network.retrofit.CodeWarsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PagerModule {

    @Provides
    @Singleton
    fun providePager(codeWarsApi: CodeWarsApi): Pager<Int, CodeChallengeOverview> {
        return Pager(
            PagingConfig(pageSize = 2)
        ) {
            CodeChallengesPagingSource(codeWarsApi)
        }
    }
}
