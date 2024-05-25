package com.test.app.common.network.model

import com.google.gson.annotations.SerializedName

data class ApiError(
    @SerializedName("success") var success: Boolean,
    @SerializedName("reason") var reason: String
)
