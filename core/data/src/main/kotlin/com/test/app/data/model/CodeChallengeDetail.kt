package com.test.app.data.model

import com.test.app.data.utils.DATE_TIME_PATTERN
import com.test.app.data.utils.formatDateToPattern
import com.test.app.model.data.StockDetails
import com.test.app.network.model.NetworkCodeChallengeDetail

fun NetworkCodeChallengeDetail.asExternalModel() = StockDetails(
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
    publishedAt = publishedAt?.formatDateToPattern(DATE_TIME_PATTERN),
    approvedAt = approvedAt?.formatDateToPattern(DATE_TIME_PATTERN),
)