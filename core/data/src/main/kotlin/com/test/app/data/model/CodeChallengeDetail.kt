package com.test.app.data.model

import com.test.app.model.data.CodeChallengeDetail
import com.test.app.network.model.NetworkCodeChallengeDetail

fun NetworkCodeChallengeDetail.asExternalModel() = CodeChallengeDetail(
    id = id,
    name = name,
    slug = slug,
    url = url,
    category = category,
    description = description,
    tags = tags,
    languages = languages,
    rank = rank?.asExternalModel(),
    createdBy = createdBy?.asExternalModel(),
    approvedBy = approvedBy?.asExternalModel(),
    totalAttempts = totalAttempts,
    totalCompleted = totalCompleted,
    totalStars = totalStars,
    voteScore = voteScore,
    publishedAt = publishedAt,
    approvedAt = approvedAt,
)