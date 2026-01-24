package com.test.app.codewars.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.test.app.common.navigation.Screen
import com.test.app.details.StockDetailsRoute
import com.test.app.list.StocksListRoute

@Composable
fun StocksNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.StocksList
    ) {
        composable<Screen.StocksList> {
            StocksListRoute(onStockClick = { stockTicker ->
                navController.navigate(Screen.StocksDetail(stockTicker))
            })
        }

        composable<Screen.StocksDetail> {
            StockDetailsRoute(
                onBackButtonClick = {
                    navController.navigateUp()
                }
            )
        }
    }
}
