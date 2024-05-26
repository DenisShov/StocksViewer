package com.test.app.data.utils

import org.joda.time.DateTime

const val DATE_TIME_PATTERN = "yyyy-MM-dd"

fun String.formatDateToPattern(pattern: String): String {
    return if (this.trim().isNotEmpty()) {
        DateTime.parse(this).toString(pattern)
    } else {
        this
    }
}
