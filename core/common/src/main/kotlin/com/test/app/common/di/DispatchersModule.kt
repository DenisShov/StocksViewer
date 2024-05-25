package com.test.app.common.di

import com.test.app.common.dispatcher.DefaultDispatcherProvider
import com.test.app.common.dispatcher.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {

    @Provides
    @Singleton
    fun provideDispatcher(): DispatcherProvider {
        return DefaultDispatcherProvider()
    }
}
