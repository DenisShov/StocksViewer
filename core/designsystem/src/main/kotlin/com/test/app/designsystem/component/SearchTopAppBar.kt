package com.test.app.designsystem.component

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.test.app.designsystem.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopAppBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchClose: () -> Unit,
    onSearchOpen: () -> Unit,
    isSearching: Boolean,
) {
    TopAppBar(
        title = {
            if (isSearching) {
                TextField(
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    placeholder = { Text("Search stocks") },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text(
                    text = stringResource(id = com.test.app.commonresources.R.string.all_stocks),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        actions = @Composable {
            if (isSearching) {
                IconButton(onClick = onSearchClose) {
                    Icon(Icons.Default.Close, contentDescription = "Close search")
                }
            } else {
                IconButton(onClick = onSearchOpen) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            }
        },
        windowInsets = WindowInsets(0)
    )
}

@ThemePreviews
@Composable
fun SearchTopAppBarPreview() {
    AppTheme {
        SearchTopAppBar(
            query = "query",
            onQueryChange = {},
            onSearchClose = {},
            onSearchOpen = {},
            isSearching = true,
        )
    }
}
