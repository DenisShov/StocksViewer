package com.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.core.designsystem.theme.AppTheme

@Composable
fun TagsRow(modifier: Modifier, values: List<String> = listOf()) {
    LazyRow(modifier = modifier.wrapContentWidth()) {
        items(count = values.size) { index ->
            Text(
                modifier = modifier
                    .padding(vertical = 8.dp, horizontal = 4.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                text = values[index],
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@com.core.designsystem.component.ThemePreviews
@Composable
fun TagsRowPreview() {
    AppTheme {
        TagsRow(modifier = Modifier, listOf("kotlin", "javascript", "python"))
    }
}
