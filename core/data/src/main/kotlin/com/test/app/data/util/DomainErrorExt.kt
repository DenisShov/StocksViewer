package com.test.app.data.util

import android.content.Context
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

fun DomainError.toErrorMessage(context: Context): String {
    return when (this) {
        is DomainError.MissingNetworkConnection -> context.resources.getString(com.test.app.commonresources.R.string.no_network_connection)

        is DomainError.HttpError -> message ?: context.resources.getString(com.test.app.commonresources.R.string.some_server_problem)

        else -> context.resources.getString(com.test.app.commonresources.R.string.something_went_wrong)
    }
}
