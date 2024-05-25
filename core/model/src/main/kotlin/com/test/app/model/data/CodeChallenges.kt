package com.test.app.model.data

data class CodeChallenges(
    var totalPages: Int = 0,
    var totalItems: Int = 0,
    var data: ArrayList<CodeChallengeOverview> = arrayListOf()
)
