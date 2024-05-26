package com.test.app.codewars.navigation

import androidx.compose.runtime.Composable
import com.test.app.common.navigation.Screen
import com.test.app.details.CodeChallengeDetailRoute
import com.test.app.list.CodeChallengesRoute
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.NavHost

@Composable
fun CodeWarsNavHost(navController: NavController<Screen>) {
    NavHost(
        controller = navController,
    ) { route ->
        when (route) {
            is Screen.CompletedChallengesList -> {
                CodeChallengesRoute(
                    navController = navController,
                )
            }

            is Screen.CompletedChallengesDetail -> {
                CodeChallengeDetailRoute(
                    codeChallengeId = route.challengeId,
                    navController = navController
                )
            }
        }
    }
}
