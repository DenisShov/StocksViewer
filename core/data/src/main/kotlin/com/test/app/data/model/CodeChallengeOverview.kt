package com.test.app.data.model

import com.test.app.model.data.CodeChallengeOverview
import com.test.app.network.model.NetworkCodeChallengeOverview

fun NetworkCodeChallengeOverview.asExternalModel() = CodeChallengeOverview(
    id = id,
    name = name,
    slug = slug,
    completedAt = completedAt,
    completedLanguages = completedLanguages
)
