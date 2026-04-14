package com.core.common.mapper

import com.core.common.error.DomainError
import com.core.commonresources.R
import core.commonresources.StringProvider
import javax.inject.Inject

class ErrorMapper @Inject constructor(private val stringProvider: StringProvider) {

    fun mapToStringError(error: DomainError): String = when (error) {
        is DomainError.HttpError -> stringProvider.getString(R.string.some_server_problem)
        is DomainError.MissingNetworkConnection -> stringProvider.getString(R.string.no_network_connection)
        is DomainError.GeneralError -> stringProvider.getString(R.string.something_went_wrong)
    }
}
