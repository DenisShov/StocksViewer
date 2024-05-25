package com.test.app.model.data

data class CodeChallengeDetail(
    var id: String = "",
    var name: String = "",
    var slug: String = "",
    var url: String = "",
    var category: String = "",
    var description: String = "",
    var tags: ArrayList<String> = arrayListOf(),
    var languages: ArrayList<String> = arrayListOf(),
    var rank: Rank? = null,
    var createdBy: CreatedBy? = null,
    var approvedBy: ApprovedBy? = null,
    var totalAttempts: Int = 0,
    var totalCompleted: Int = 0,
    var totalStars: Int = 0,
    var voteScore: Int = 0,
    var publishedAt: String = "",
    var approvedAt: String = ""
)
