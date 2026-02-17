package com.test.app.list

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.test.app.designsystem.component.BackgroundPreview
import com.test.app.designsystem.component.SearchTopAppBar
import com.test.app.designsystem.theme.AppTheme
import com.test.app.list.model.TickerUiModel
import com.test.app.ui.ErrorRetryItem
import com.test.app.ui.showSnackBar
import kotlinx.coroutines.flow.flowOf

@Composable
fun StocksListRoute(
    viewModel: StocksListViewModel = hiltViewModel(),
    onStockClick: (String) -> Unit,
) {
    val stocksPaging = viewModel.stocksPaging.collectAsLazyPagingItems()

    val pullToRefreshState = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val someErrorHappened = stringResource(id = com.test.app.commonresources.R.string.some_error_happened)
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
            )
        },
        onSearchQueryChange = viewModel::onSearchQueryChange
    )
}

@Composable
fun StocksListScreen(
    stocksPaging: LazyPagingItems<TickerUiModel>,
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
        contentWindowInsets = WindowInsets.navigationBars,
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
                onSearchOpen = {
                    isSearching = true
                },
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
    stocksPaging: LazyPagingItems<TickerUiModel>,
    onStockClick: (String) -> Unit,
    onRefreshError: () -> Unit,
    isRefreshing: Boolean,
    pullToRefreshState: PullToRefreshState,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        if (stocksPaging.loadState.refresh is LoadState.Loading) {
            StocksListSkeleton()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(top = 16.dp),
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

@Composable
fun StocksListSkeleton() {
    val infiniteTransition = rememberInfiniteTransition(label = "skeleton")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp),
    ) {
        items(10) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                )
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha))
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .height(20.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha))
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .width(80.dp)
                                .height(16.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha))
                        )
                    }
                }
            }
        }
    }
}

@BackgroundPreview
@Composable
fun StocksListPreview() {
    AppTheme {
        val mockStocks = flowOf(
            PagingData.from(
                listOf(
                    TickerUiModel("AAPL", "Apple Inc.", "Common Stock"),
                    TickerUiModel("GOOGL", "Alphabet Inc.", "Common Stock"),
                    TickerUiModel("MSFT", "Microsoft Corporation", "Common Stock")
                )
            )
        ).collectAsLazyPagingItems()
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 16.dp),
        ) {
            items(count = mockStocks.itemCount) { index ->
                mockStocks[index]?.let {
                    StockListItem(stockItem = it)
                }
            }
        }
    }
}

@BackgroundPreview
@Composable
fun StocksListSkeletonPreview() {
    AppTheme {
        StocksListSkeleton()
    }
}
