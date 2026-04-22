package com.feature.list.impl.ui.paging

sealed class SearchResultsError : Throwable() {
    data object NetworkError : SearchResultsError()
    data class HttpError(val errorMessage: String?) : SearchResultsError()
    data object UnknownError : SearchResultsError()
}
