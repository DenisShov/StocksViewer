package com.test.app.codewars.navigation

import androidx.compose.runtime.Composable
import com.test.app.common.navigation.Screen
import com.test.app.details.StockDetailsRoute
import com.test.app.list.StocksListRoute
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.NavHost
import dev.olshevski.navigation.reimagined.navigate
import dev.olshevski.navigation.reimagined.pop

@Composable
fun StocksNavHost(navController: NavController<Screen>) {
    NavHost(
        controller = navController,
    ) { route ->
        when (route) {
            is Screen.StocksList -> {
                StocksListRoute(onStockClick = { stockTicker ->
                    navController.navigate(
                        Screen.StocksDetail(
                            stockTicker
                        )
                    )
                })
            }

            is Screen.StocksDetail -> {
                StockDetailsRoute(
                    stockTicker = route.stockTicker,
                    onBackButtonClick = {
                        navController.pop()
                    }
                )
            }
        }
    }
}
