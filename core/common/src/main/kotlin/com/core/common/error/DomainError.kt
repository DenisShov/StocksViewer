package com.core.common.error

sealed class DomainError {
    data object MissingNetworkConnection : DomainError()
    data class HttpError(val code: Int, val message: String?) : DomainError()
    data class GeneralError(val exception: Throwable) : DomainError()
}
