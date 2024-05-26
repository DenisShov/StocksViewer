package com.test.app.designsystem.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.test.app.designsystem.R
import com.test.app.designsystem.theme.AppTheme

@Composable
fun CodeWarsTitleLarge(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    Text(
        modifier = modifier,
        text = stringResource(id = R.string.app_name),
        style = MaterialTheme.typography.titleLarge,
        color = color
    )
}

@ThemePreviews
@Composable
fun CodeWarsTitleLargePreview() {
    AppTheme {
        CodeWarsTitleLarge(modifier = Modifier)
    }
}
