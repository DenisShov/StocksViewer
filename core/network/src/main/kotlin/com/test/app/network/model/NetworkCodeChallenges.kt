package com.test.app.network.model

import com.google.gson.annotations.SerializedName

data class NetworkCodeChallenges(
    @SerializedName("totalPages") var totalPages: Int = 0,
    @SerializedName("totalItems") var totalItems: Int = 0,
    @SerializedName("data") var data: ArrayList<NetworkCodeChallengeOverview> = arrayListOf()
)
