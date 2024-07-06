package com.test.app.codewars.navigation

import androidx.compose.runtime.Composable
import com.test.app.common.navigation.Screen
import com.test.app.details.CodeChallengeDetailRoute
import com.test.app.list.CodeChallengesRoute
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.NavHost
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.pop

@Composable
fun CodeWarsNavHost(navController: NavController<Screen>) {
    NavHost(
        controller = navController,
    ) { route ->
        when (route) {
            is Screen.CompletedChallengesList -> {
                CodeChallengesRoute(onChallengeClick = { challengeOverviewId ->
                    navController.navigate(
                        Screen.CompletedChallengesDetail(
                            challengeOverviewId
                        )
                    )
                })
            }

            is Screen.CompletedChallengesDetail -> {
                CodeChallengeDetailRoute(
                    codeChallengeId = route.challengeId,
                    onBackButtonClick = {
                        navController.pop()
                    }
                )
            }
        }
    }
}
