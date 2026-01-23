package com.test.app.data.util

import arrow.core.Either
import com.test.app.common.error.DomainError
import com.test.app.network.model.ApiError
import java.net.ConnectException
import java.net.UnknownHostException

fun <T> Either<ApiError, T>.mapLeftToDomainError(): Either<DomainError, T> = mapLeft {
    it.toDomainError()
}

private fun ApiError.toDomainError(): DomainError = when (this) {
    is ApiError.HttpError -> DomainError.HttpError(code, message)
    is ApiError.NetworkError -> {
        when (throwable) {
            is UnknownHostException, is ConnectException -> DomainError.MissingNetworkConnection
            else -> DomainError.GeneralError(throwable)
        }
    }

    is ApiError.UnknownError -> DomainError.GeneralError(throwable)
}