package com.test.app.network.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.test.app.network.BuildConfig
import com.test.app.network.retrofit.ApiErrorInterceptor
import com.test.app.network.retrofit.CodeWarsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .create()
    }

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addLoggingInterceptor(BuildConfig.DEBUG)
            .addInterceptor(ApiErrorInterceptor())
            .build()
    }

    private fun OkHttpClient.Builder.addLoggingInterceptor(isLogEnabled: Boolean) =
        apply {
            if (!isLogEnabled) {
                return@apply
            }
            val loggingInterceptor =
                HttpLoggingInterceptor { message -> Timber.i(message) }
                    .apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }

            addInterceptor(loggingInterceptor)
        }

    @Provides
    @Singleton
    fun provideCodeWarsApi(
    okHttpClient: OkHttpClient,
    gson: Gson,
    ): CodeWarsApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BACKEND_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(CodeWarsApi::class.java)
    }
}
