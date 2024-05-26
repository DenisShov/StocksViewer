package com.test.app.ui

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun showSnackBar(
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    message: String,
    actionLabel: String,
    actionPerformed: () -> Unit,
    dismissed: () -> Unit
) {
    scope.launch {
        val snackBarResult = snackBarHostState.showSnackbar(
            message = message,
            actionLabel = actionLabel,
            duration = SnackbarDuration.Indefinite
        )
        when (snackBarResult) {
            SnackbarResult.ActionPerformed -> actionPerformed.invoke()
            SnackbarResult.Dismissed -> dismissed.invoke()
        }
    }
}
