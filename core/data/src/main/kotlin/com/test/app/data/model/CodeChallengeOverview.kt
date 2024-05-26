package com.test.app.data.model

import com.test.app.data.utils.DATE_TIME_PATTERN
import com.test.app.data.utils.formatDateToPattern
import com.test.app.model.data.CodeChallengeOverview
import com.test.app.network.model.NetworkCodeChallengeOverview

fun NetworkCodeChallengeOverview.asExternalModel() = CodeChallengeOverview(
    id = id,
    name = name,
    slug = slug,
    completedAt = completedAt.formatDateToPattern(DATE_TIME_PATTERN),
    completedLanguages = completedLanguages
)
