package com.test.app.data.paging

sealed class SearchResultsError : Throwable() {
    data object NetworkError : SearchResultsError()
    data class HttpError(val errorMessage: String?) : SearchResultsError()
    data object UnknownError : SearchResultsError()
}
