package com.test.app.data.model

import com.test.app.model.data.Rank
import com.test.app.network.model.NetworkRank

fun NetworkRank.toDomain() = Rank(
    id = id,
    name = name,
    color = color
)
