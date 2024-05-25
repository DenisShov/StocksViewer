package com.test.app.model.data

data class CodeChallengeOverview(
    var id: String = "",
    var name: String = "",
    var slug: String = "",
    var completedAt: String = "",
    var completedLanguages: ArrayList<String> = arrayListOf()
)
