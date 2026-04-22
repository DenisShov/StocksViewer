package com.core.network.retrofit

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.core.network.model.errors.ApiError
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * A Retrofit CallAdapter.Factory that converts responses to Either<ApiError, T>.
 *
 * - HTTP errors (4xx, 5xx) -> ApiError.HttpError
 * - Network/IO exceptions -> ApiError.NetworkError
 * - Other exceptions -> ApiError.UnknownError
 */
class EitherCallAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) {
            return null
        }

        val callType = getParameterUpperBound(0, returnType as ParameterizedType)
        if (getRawType(callType) != Either::class.java) {
            return null
        }

        val eitherType = callType as ParameterizedType
        val errorType = getParameterUpperBound(0, eitherType)
        if (getRawType(errorType) != ApiError::class.java) {
            return null
        }

        val successType = getParameterUpperBound(1, eitherType)
        return EitherCallAdapter<Any>(successType)
    }

    private class EitherCallAdapter<R>(
        private val successType: Type,
    ) : CallAdapter<R, Call<Either<ApiError, R>>> {

        override fun responseType(): Type = successType

        override fun adapt(call: Call<R>): Call<Either<ApiError, R>> {
            return EitherCall(call)
        }
    }

    private class EitherCall<R>(
        private val delegate: Call<R>,
    ) : Call<Either<ApiError, R>> {

        override fun enqueue(callback: Callback<Either<ApiError, R>>) {
            delegate.enqueue(object : Callback<R> {
                override fun onResponse(call: Call<R>, response: Response<R>) {
                    callback.onResponse(this@EitherCall, Response.success(response.toEither()))
                }

                override fun onFailure(call: Call<R>, t: Throwable) {
                    val error = when (t) {
                        is IOException -> ApiError.NetworkError(t)
                        else -> ApiError.UnknownError(t)
                    }
                    callback.onResponse(this@EitherCall, Response.success(error.left()))
                }
            })
        }

        @Suppress("TooGenericExceptionCaught")
        override fun execute(): Response<Either<ApiError, R>> {
            return try {
                val response = delegate.execute()
                Response.success(response.toEither())
            } catch (e: IOException) {
                Response.success(ApiError.NetworkError(e).left())
            } catch (e: Exception) {
                Response.success(ApiError.UnknownError(e).left())
            }
        }

        override fun clone(): Call<Either<ApiError, R>> = EitherCall(delegate.clone())
        override fun isExecuted(): Boolean = delegate.isExecuted
        override fun cancel() = delegate.cancel()
        override fun isCanceled(): Boolean = delegate.isCanceled
        override fun request(): Request = delegate.request()
        override fun timeout(): Timeout = delegate.timeout()

        private fun Response<R>.toEither(): Either<ApiError.HttpError, R & Any> {
            val result = if (isSuccessful) {
                val body = body()
                if (body != null) {
                    body.right()
                } else {
                    ApiError.HttpError(
                        code = code(),
                        message = "Response body is null",
                        body = null
                    ).left()
                }
            } else {
                val errorBodyString = errorBody()?.string()
                val errorMessage = getErrorMessage(errorBodyString)

                ApiError.HttpError(
                    code = code(),
                    message = errorMessage,
                    body = errorBodyString,
                ).left()
            }
            return result
        }

        private fun Response<R>.getErrorMessage(errorBodyString: String?): String? = errorBodyString?.let {
            """"error":"([^"]+)"""".toRegex().find(it)?.groupValues?.get(1)
        } ?: message()
    }
}
