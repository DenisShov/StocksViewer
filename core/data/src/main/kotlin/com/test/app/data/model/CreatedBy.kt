package com.test.app.data.model

import com.test.app.model.data.CreatedBy
import com.test.app.network.model.NetworkCreatedBy

fun NetworkCreatedBy.asExternalModel() = CreatedBy(
    username = username,
    url = url
)
