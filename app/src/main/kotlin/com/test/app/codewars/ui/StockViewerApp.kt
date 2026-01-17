package com.test.app.codewars.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.test.app.codewars.navigation.StocksNavHost
import com.test.app.common.navigation.Screen
import dev.olshevski.navigation.reimagined.NavBackHandler
import dev.olshevski.navigation.reimagined.rememberNavController

@Composable
fun StockViewerApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController<Screen>(
        startDestination = Screen.StocksList,
    )

    NavBackHandler(navController)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            StocksNavHost(navController)
        }
    }

}