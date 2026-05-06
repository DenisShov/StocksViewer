package com.core.network.di

import com.core.network.BuildConfig
import com.core.network.retrofit.EitherCallAdapterFactory
import com.core.network.retrofit.StocksApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

val networkModule = module {
    single<Gson> {
        GsonBuilder()
            .create()
    }

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .addApiKeyInterceptor(BuildConfig.API_KEY)
            .addLoggingInterceptor(BuildConfig.DEBUG)
            .build()
    }

    single<StocksApi> {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BACKEND_URL)
            .client(get())
            .addCallAdapterFactory(EitherCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
            .create(StocksApi::class.java)
    }
}

private fun OkHttpClient.Builder.addApiKeyInterceptor(apiKey: String) =
    apply {
        addInterceptor { chain ->
            val url = chain.request().url.newBuilder()
                .addQueryParameter("apiKey", apiKey)
                .build()
            chain.proceed(chain.request().newBuilder().url(url).build())
        }
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
