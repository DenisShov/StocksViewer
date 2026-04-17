package com.test.app.stockviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import com.core.designsystem.theme.AppTheme
import com.test.app.stockviewer.ui.StockViewerApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(darkTheme = isSystemInDarkTheme()) {
                StockViewerApp()
            }
        }
    }
}
