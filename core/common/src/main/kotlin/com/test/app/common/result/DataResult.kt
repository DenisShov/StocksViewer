package com.test.app.common.result

import com.test.app.common.error.AppError
import com.test.app.common.network.exceptions.ApiException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import java.net.ConnectException
import java.net.UnknownHostException

sealed interface DataResult<out T> {
    data class Success<T>(val data: T) : DataResult<T>

    data class Failure(val error: AppError) : DataResult<Nothing>

    data object Loading : DataResult<Nothing>
}

fun <T> DataResult<T>.fold(
    onSuccess: (T) -> Unit,
    onFailure: (AppError) -> Unit,
    onLoading: () -> Unit,
) {
    when (this) {
        is DataResult.Success -> onSuccess(data)
        is DataResult.Failure -> onFailure(error)
        is DataResult.Loading -> onLoading()
    }
}

fun <T> Flow<T>.asDataResult(): Flow<DataResult<T>> {
    return this
        .map<T, DataResult<T>> {
            DataResult.Success(it)
        }
        .onStart { emit(DataResult.Loading) }
        .catch { emit(DataResult.Failure(it.toError)) }
}

val Throwable.toError: AppError
    get() =
        when (this) {
            is UnknownHostException -> AppError.MissingNetworkConnection
            is ConnectException -> AppError.MissingNetworkConnection
            is ApiException -> AppError.ApiError(this)
            else -> AppError.GeneralError(this)
        }
