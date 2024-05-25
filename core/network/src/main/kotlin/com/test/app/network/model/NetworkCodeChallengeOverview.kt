package com.test.app.network.model

import com.google.gson.annotations.SerializedName

data class NetworkCodeChallengeOverview(
    @SerializedName("id") var id: String = "",
    @SerializedName("name") var name: String = "",
    @SerializedName("slug") var slug: String = "",
    @SerializedName("completedAt") var completedAt: String = "",
    @SerializedName("completedLanguages") var completedLanguages: List<String> = listOf()
)
