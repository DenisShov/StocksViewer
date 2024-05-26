package com.test.app.codewars.navigation

import androidx.compose.runtime.Composable
import com.test.app.common.navigation.Screen
import com.test.app.list.CodeChallengesScreen
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.NavHost

@Composable
fun CodeWarsNavHost(navController: NavController<Screen>) {
    NavHost(
        controller = navController,
    ) { route ->
        when (route) {
            is Screen.CompletedChallengesList -> {
                CodeChallengesScreen(
                    navController = navController,
                )
            }

            is Screen.CompletedChallengesDetail -> {
//                CoinDetailScreen(
//                    coinMarkets = route.coinMarkets,
//                    navController = navController,
//                )
            }
        }
    }
}
