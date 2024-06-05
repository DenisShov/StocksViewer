package com.test.app.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.test.app.designsystem.component.ThemePreviews
import com.test.app.designsystem.theme.AppTheme

@Composable
fun ErrorRetryItem(error: String?, onTryClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .wrapContentHeight()
                .padding(vertical = 8.dp),
            text = error
                ?: stringResource(id = com.test.app.commonresources.R.string.some_error_happened),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.titleMedium
        )
        Button(
            modifier = Modifier
                .wrapContentWidth(),
            onClick = onTryClicked
        ) {
            Text(
                text = stringResource(id = com.test.app.commonresources.R.string.retry),
                color = Color.White,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@ThemePreviews
@Composable
fun ErrorRetryItemPreview() {
    AppTheme {
        ErrorRetryItem(error = null) {}
    }
}
