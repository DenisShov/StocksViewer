package com.test.app.common.error

import com.test.app.common.network.exceptions.ApiException

sealed class AppError {
    data object MissingNetworkConnection : AppError()

    data class ApiError(val exception: ApiException) : AppError()

    data class GeneralError(val exception: Throwable) : AppError()
}
