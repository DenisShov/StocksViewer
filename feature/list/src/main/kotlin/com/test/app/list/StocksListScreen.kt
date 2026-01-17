@file:OptIn(ExperimentalMaterial3Api::class)

package com.test.app.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.test.app.designsystem.component.SearchTopAppBar
import com.test.app.model.data.Ticker
import com.test.app.ui.ErrorRetryItem
import com.test.app.ui.showSnackBar
import dev.olshevski.navigation.reimagined.hilt.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StocksListRoute(
    viewModel: StocksListViewModel = hiltViewModel(),
    onStockClick: (String) -> Unit = {}
) {
    val stocksPaging = viewModel.stocksPaging.collectAsLazyPagingItems()

    val pullToRefreshState = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    val someErrorHappened =
        stringResource(id = com.test.app.commonresources.R.string.some_error_happened)
    val tryAgain = stringResource(id = com.test.app.commonresources.R.string.try_again)

    StocksListScreen(
        stocksPaging = stocksPaging,
        snackBarHostState = snackBarHostState,
        pullToRefreshState = pullToRefreshState,
        isRefreshing = isRefreshing,
        onStockClick = onStockClick,
        onRefreshError = {
            showSnackBar(
                scope = scope,
                snackBarHostState = snackBarHostState,
                message = (stocksPaging.loadState.refresh as LoadState.Error).error.message
                    ?: someErrorHappened,
                actionLabel = tryAgain,
                actionPerformed = { stocksPaging.refresh() },
                dismissed = {}
            )
        },
        onSearchQueryChange = viewModel::onSearchQueryChange
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StocksListScreen(
    stocksPaging: LazyPagingItems<Ticker>,
    snackBarHostState: SnackbarHostState,
    pullToRefreshState: PullToRefreshState,
    isRefreshing: Boolean,
    onStockClick: (String) -> Unit = {},
    onRefreshError: () -> Unit = {},
    onSearchQueryChange: (String) -> Unit = {},
) {
    var isSearching by rememberSaveable { mutableStateOf(false) }
    var query by rememberSaveable { mutableStateOf("") }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .pullToRefresh(
                state = pullToRefreshState,
                isRefreshing = isRefreshing,
                onRefresh = { stocksPaging.refresh() },
            ),
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            SearchTopAppBar(
                query = query,
                onQueryChange = {
                    query = it
                    onSearchQueryChange(it)
                },
                onSearchClose = {
                    query = ""
                    isSearching = false
                    onSearchQueryChange("")
                },
                onSearchOpen = { isSearching = true },
                isSearching = isSearching,
            )
        },
        content = { padding ->
            StocksListContent(
                stocksPaging = stocksPaging,
                onStockClick = onStockClick,
                onRefreshError = onRefreshError,
                isRefreshing = isRefreshing,
                pullToRefreshState = pullToRefreshState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            )
        }
    )
}

@Composable
private fun StocksListContent(
    stocksPaging: LazyPagingItems<Ticker>,
    onStockClick: (String) -> Unit,
    onRefreshError: () -> Unit,
    isRefreshing: Boolean,
    pullToRefreshState: PullToRefreshState,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        if (stocksPaging.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(vertical = 6.dp)
                    .testTag("CircularProgressIndicator"),
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(count = stocksPaging.itemCount) { index ->
                    stocksPaging[index]?.let {
                        StockListItem(
                            stockItem = it,
                            onStockClick = onStockClick
                        )
                    }
                }

                when {
                    stocksPaging.loadState.append is LoadState.Loading -> {
                        item {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(vertical = 6.dp)
                                    .testTag("AppendLoading")
                            )
                        }
                    }

                    stocksPaging.loadState.append is LoadState.Error -> {
                        item {
                            ErrorRetryItem(
                                error = (stocksPaging.loadState.append as LoadState.Error).error.message,
                                onTryClicked = {
                                    stocksPaging.retry()
                                }
                            )
                        }
                    }

                    stocksPaging.loadState.refresh is LoadState.Error -> {
                        onRefreshError.invoke()
                    }
                }
            }
            if (isRefreshing) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                )
            } else {
                LinearProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth(),
                    progress = { pullToRefreshState.distanceFraction },
                )
            }
        }
    }
}
