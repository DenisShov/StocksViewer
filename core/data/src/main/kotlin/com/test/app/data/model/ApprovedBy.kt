package com.test.app.data.model

import com.test.app.model.data.ApprovedBy
import com.test.app.network.model.NetworkApprovedBy

fun NetworkApprovedBy.toDomain() = ApprovedBy(
    username = username,
    url = url
)
