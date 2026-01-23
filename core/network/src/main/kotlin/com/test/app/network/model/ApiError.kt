package com.test.app.network.model

/**
 * Represents errors that can occur during network calls.
 */
sealed class ApiError {
    data class HttpError(val code: Int, val message: String, val body: String?) : ApiError()
    data class NetworkError(val throwable: Throwable) : ApiError()
    data class UnknownError(val throwable: Throwable) : ApiError()
}
