package com.test.app.common.result

import android.content.Context
import com.test.app.common.error.DomainError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import java.net.ConnectException
import java.net.UnknownHostException

sealed interface DataResult<out T> {
    data class Success<T>(val data: T) : DataResult<T>

    data class Failure(val error: DomainError) : DataResult<Nothing>

    data object Loading : DataResult<Nothing>
}

fun <T> DataResult<T>.fold(
    onSuccess: (T) -> Unit,
    onFailure: (DomainError) -> Unit,
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

val Throwable.toError: DomainError
    get() =
        when (this) {
            is UnknownHostException -> DomainError.MissingNetworkConnection
            is ConnectException -> DomainError.MissingNetworkConnection
            else -> DomainError.GeneralError(this)
        }

fun DomainError.toErrorMessage(context: Context): String {
    return when (this) {
        is DomainError.MissingNetworkConnection -> context.resources.getString(com.test.app.commonresources.R.string.no_network_connection)
        is DomainError.HttpError -> context.resources.getString(com.test.app.commonresources.R.string.some_server_problem)
        else -> context.resources.getString(com.test.app.commonresources.R.string.something_went_wrong)
    }
}