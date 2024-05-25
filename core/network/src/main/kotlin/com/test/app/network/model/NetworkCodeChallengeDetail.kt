package com.test.app.network.model

import com.google.gson.annotations.SerializedName

data class NetworkCodeChallengeDetail(
    @SerializedName("id") var id: String = "",
    @SerializedName("name") var name: String = "",
    @SerializedName("slug") var slug: String = "",
    @SerializedName("url") var url: String = "",
    @SerializedName("category") var category: String = "",
    @SerializedName("description") var description: String = "",
    @SerializedName("tags") var tags: ArrayList<String> = arrayListOf(),
    @SerializedName("languages") var languages: ArrayList<String> = arrayListOf(),
    @SerializedName("rank") var rank: NetworkRank? = null,
    @SerializedName("createdBy") var createdBy: NetworkCreatedBy? = null,
    @SerializedName("approvedBy") var approvedBy: NetworkApprovedBy? = null,
    @SerializedName("totalAttempts") var totalAttempts: Int = 0,
    @SerializedName("totalCompleted") var totalCompleted: Int = 0,
    @SerializedName("totalStars") var totalStars: Int = 0,
    @SerializedName("voteScore") var voteScore: Int = 0,
    @SerializedName("publishedAt") var publishedAt: String = "",
    @SerializedName("approvedAt") var approvedAt: String = ""
)
