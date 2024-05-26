package com.test.app.common.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class Screen : Parcelable {
    @Parcelize
    data object CompletedChallengesList : Screen()

    @Parcelize
    data class CompletedChallengesDetail(val challengeId: String) : Screen()
}
