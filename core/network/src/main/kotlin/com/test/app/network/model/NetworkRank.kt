package com.test.app.network.model

import com.google.gson.annotations.SerializedName

data class NetworkRank(
    @SerializedName("id") var id: Int = 0,
    @SerializedName("name") var name: String? = "",
    @SerializedName("color") var color: String? = ""
)
