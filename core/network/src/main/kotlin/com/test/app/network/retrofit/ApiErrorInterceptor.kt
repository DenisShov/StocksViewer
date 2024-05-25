package com.test.app.network.retrofit

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class ApiErrorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (response.isSuccessful.not()) {
            val responseBody = response.body
            when (val errorResponse = responseBody?.string()) {
                null -> throw ApiException()
                else -> {
                    val apiError: ApiError = getApiError(errorResponse)
                    Timber.e("Faced an apiError: $apiError")
                    throw ApiException(apiError)
                }
            }
        }
        return response
    }

    private fun getApiError(errorResponse: String): ApiError =
        Gson().fromJson(errorResponse, object : TypeToken<ApiError>() {}.type)
}
