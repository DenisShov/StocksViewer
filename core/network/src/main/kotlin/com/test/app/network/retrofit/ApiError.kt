package com.test.app.network.retrofit

import com.google.gson.annotations.SerializedName
import java.io.IOException

data class ApiError(
    @SerializedName("success") var success: Boolean,
    @SerializedName("reason") var reason: String
)

class ApiException(val error: ApiError? = null) : IOException()
