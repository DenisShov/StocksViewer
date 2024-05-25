package com.test.app.network.model

import com.google.gson.annotations.SerializedName

data class NetworkCreatedBy(
    @SerializedName("username") var username: String = "",
    @SerializedName("url") var url: String = ""
)
