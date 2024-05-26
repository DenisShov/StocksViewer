@file:OptIn(ExperimentalMaterial3Api::class)

package com.test.app.list

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.test.app.common.navigation.Screen
import com.test.app.designsystem.component.BackgroundPreview
import com.test.app.designsystem.component.CodeWarsTitleLarge
import com.test.app.designsystem.theme.AppTheme
import com.test.app.model.data.CodeChallengeOverview
import com.test.app.ui.ErrorRetryItem
import com.test.app.ui.TagsRow
import com.test.app.ui.showSnackBar
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.hilt.hiltViewModel
import dev.olshevski.navigation.reimagined.navigate
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeChallengesRoute(
    navController: NavController<Screen>,
    viewModel: CodeChallengesViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val codeChallenges = viewModel.codeChallenges.collectAsLazyPagingItems()

    val pullToRefreshState = rememberPullToRefreshState()
    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            codeChallenges.refresh()
            pullToRefreshState.endRefresh()
        }
    }

    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    CodeChallengesScreen(
        context,
        navController,
        codeChallenges,
        snackBarHostState,
        pullToRefreshState,
        scope
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeChallengesScreen(
    context: Context,
    navController: NavController<Screen>,
    codeChallenges: LazyPagingItems<CodeChallengeOverview>,
    snackBarHostState: SnackbarHostState,
    pullToRefreshState: PullToRefreshState,
    scope: CoroutineScope
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(pullToRefreshState.nestedScrollConnection),
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            CenterAlignedTopAppBar(title = {
                CodeWarsTitleLarge()
            })
        },
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                if (codeChallenges.loadState.refresh is LoadState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(vertical = 6.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(count = codeChallenges.itemCount) { index ->
                            codeChallenges[index]?.let {
                                ChallengeOverviewItem(
                                    challengeOverview = it,
                                    onChallengeOverviewClick = { challengeOverview ->
                                        navController.navigate(
                                            Screen.CompletedChallengesDetail(
                                                challengeOverview.id
                                            )
                                        )
                                    })
                            }
                        }

                        when {
                            codeChallenges.loadState.append is LoadState.Loading -> {
                                item {
                                    CircularProgressIndicator(
                                        modifier = Modifier.padding(vertical = 6.dp)
                                    )
                                }
                            }

                            codeChallenges.loadState.append is LoadState.Error -> {
                                item {
                                    ErrorRetryItem(
                                        error = (codeChallenges.loadState.append as LoadState.Error).error.message,
                                        onTryClicked = {
                                            codeChallenges.retry()
                                        }
                                    )
                                }
                            }

                            codeChallenges.loadState.refresh is LoadState.Error -> {
                                showSnackBar(
                                    scope = scope,
                                    snackBarHostState = snackBarHostState,
                                    message = (codeChallenges.loadState.refresh as LoadState.Error).error.message
                                        ?: context.resources.getString(R.string.some_error_happened),
                                    actionLabel = context.resources.getString(R.string.try_again),
                                    actionPerformed = { codeChallenges.refresh() },
                                    dismissed = {}
                                )
                            }
                        }
                    }
                    if (pullToRefreshState.isRefreshing) {
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
                            progress = { pullToRefreshState.progress })
                    }
                }
            }
        }
    )
}

@Composable
fun ChallengeOverviewItem(
    challengeOverview: CodeChallengeOverview,
    onChallengeOverviewClick: (CodeChallengeOverview) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onChallengeOverviewClick(challengeOverview) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = challengeOverview.name,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
            )
            TagsRow(
                Modifier.align(Alignment.CenterHorizontally),
                challengeOverview.completedLanguages
            )
            Text(
                text = challengeOverview.completedAt,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(8.dp)
            )
        }
    }
}

@BackgroundPreview
@Composable
fun ChallengeOverviewItemPreview() {
    AppTheme {
        ChallengeOverviewItem(
            CodeChallengeOverview(
                name = "Multiples of 3 and 5",
                completedAt = "2017-04-06",
                completedLanguages = listOf(
                    "javascript",
                    "coffeescript",
                    "ruby",
                    "javascript",
                    "ruby",
                    "javascript",
                    "ruby",
                    "coffeescript",
                    "javascript",
                    "ruby",
                    "coffeescript"
                )
            )
        )
    }
}
