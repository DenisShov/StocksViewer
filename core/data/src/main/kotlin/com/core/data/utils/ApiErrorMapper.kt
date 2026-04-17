package com.core.data.utils

import arrow.core.Either
import com.core.common.error.DomainError
import com.core.network.model.ApiError
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
