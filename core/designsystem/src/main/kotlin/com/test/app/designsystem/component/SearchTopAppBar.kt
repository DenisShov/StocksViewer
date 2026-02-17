package com.test.app.designsystem.component

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
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
    CenterAlignedTopAppBar(
        title = {
            if (isSearching) {
                SearchTextField(
                    query = query,
                    onQueryChange = onQueryChange,
                )
            } else {
                Text(
                    text = stringResource(id = com.test.app.commonresources.R.string.all_stocks),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        navigationIcon = {
            if (isSearching) {
                IconButton(onClick = onSearchClose) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = com.test.app.commonresources.R.string.a11y_close_search)
                    )
                }
            }
        },
        actions = {
            if (!isSearching) {
                IconButton(onClick = onSearchOpen) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                }
            }
        },
        windowInsets = WindowInsets(),
    )
}

@Composable
fun SearchTextField(
    query: String,
    onQueryChange: (String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                stringResource(id = com.test.app.commonresources.R.string.search_stocks),
                style = MaterialTheme.typography.bodyLarge
            )
        },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
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
