package com.test.app.details

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.test.app.common.navigation.Screen
import com.test.app.common.result.toErrorMessage
import com.test.app.designsystem.component.BackgroundPreview
import com.test.app.designsystem.component.LoadingData
import com.test.app.designsystem.icon.CodeWarsIcon.ArrowBack
import com.test.app.designsystem.theme.AppTheme
import com.test.app.details.CodeChallengeDetailViewModel.Companion.CODE_CHALLENGE_ID_ARG
import com.test.app.model.data.ApprovedBy
import com.test.app.model.data.CodeChallengeDetail
import com.test.app.model.data.CreatedBy
import com.test.app.model.data.Rank
import com.test.app.ui.DevicePreviews
import com.test.app.ui.TagsRow
import com.test.app.ui.showSnackBar
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.hilt.hiltViewModel
import dev.olshevski.navigation.reimagined.pop
import kotlinx.coroutines.CoroutineScope

@Composable
fun CodeChallengeDetailRoute(
    codeChallengeId: String,
    navController: NavController<Screen>,
    viewModel: CodeChallengeDetailViewModel = hiltViewModel(
        defaultArguments = bundleOf(CODE_CHALLENGE_ID_ARG to codeChallengeId)
    )
) {
    LaunchedEffect(Unit) {
        viewModel.getCodeChallengeById()
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    CodeChallengeDetailScreen(uiState, snackBarHostState, navController, viewModel, context, scope)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CodeChallengeDetailScreen(
    uiState: CodeChallengeDetailViewModel.State,
    snackBarHostState: SnackbarHostState,
    navController: NavController<Screen>,
    viewModel: CodeChallengeDetailViewModel,
    context: Context,
    scope: CoroutineScope
) {
    Scaffold(
        topBar = {
            TopAppBar(title = {}, navigationIcon = {
                IconButton(onClick = { navController.pop() }) {
                    Icon(
                        imageVector = ArrowBack,
                        contentDescription = stringResource(id = R.string.ui_return_to_previous_screen),
                        modifier = Modifier.padding(start = 12.dp),
                    )
                }
            })
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val codeChallengeUiState = uiState.codeChallengeState) {
                is CodeChallengeDetailViewModel.CodeChallengeState.Loading -> {
                    LoadingData(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is CodeChallengeDetailViewModel.CodeChallengeState.Success -> {
                    CodeChallengeContent(codeChallengeUiState.codeChallengeDetail, context)
                }

                is CodeChallengeDetailViewModel.CodeChallengeState.Error -> {
                    Text(
                        text = stringResource(id = R.string.some_error_happened),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(align = Alignment.CenterVertically)
                            .padding(16.dp),
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                    )

                    LaunchedEffect(snackBarHostState) {
                        showSnackBar(scope = scope,
                            snackBarHostState = snackBarHostState,
                            message = codeChallengeUiState.error.toErrorMessage(context),
                            actionLabel = context.resources.getString(com.test.app.ui.R.string.retry),
                            actionPerformed = { viewModel.getCodeChallengeById() },
                            dismissed = {})
                    }
                }
            }
        }
    }
}

@Composable
private fun CodeChallengeContent(codeChallengeDetail: CodeChallengeDetail, context: Context) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = MaterialTheme.shapes.large,
            ),
    ) {

        item {
            codeChallengeDetail.name?.let {
                Title(it)
            }
            codeChallengeDetail.description?.let {
                Description(it)
            }
        }

        val list = getCodeChallengeTablePairs(codeChallengeDetail, context)
        items(list.size) { index ->
            val name = list[index].first
            val value = list[index].second
            value?.let {
                CodeChallengeRowItem(
                    name = name,
                    value = it
                )
            }
        }

        item {
            codeChallengeDetail.tags?.let {
                CodeChallengeTagsItem(
                    name = stringResource(id = R.string.tags),
                    it
                )
            }
            codeChallengeDetail.languages?.let {
                CodeChallengeTagsItem(
                    name = stringResource(id = R.string.languages),
                    it,
                    showDivider = false
                )
            }
        }
    }
}

@Composable
private fun Title(it: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = it,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge,
        )

        HorizontalDivider(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .alpha(0.5f),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.secondary,
        )
    }
}

@Composable
private fun Description(description: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = stringResource(id = R.string.description),
            style = MaterialTheme.typography.titleMedium,
        )

        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = description,
            style = MaterialTheme.typography.bodyMedium,
        )

        HorizontalDivider(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .alpha(0.2f),
            color = MaterialTheme.colorScheme.secondary,
        )
    }
}

@Composable
fun CodeChallengeRowItem(
    name: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
        )

        Spacer(modifier = Modifier.size(8.dp))

        Text(
            text = value,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyMedium,
        )
    }

    HorizontalDivider(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .alpha(0.2f),
        color = MaterialTheme.colorScheme.secondary,
    )
}

@Composable
fun CodeChallengeTagsItem(
    name: String,
    values: List<String>,
    showDivider: Boolean = true
) {
    Row(
        modifier = Modifier
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
        )

        Spacer(modifier = Modifier.size(8.dp))

        TagsRow(Modifier, values)
    }

    if (showDivider) {
        HorizontalDivider(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .alpha(0.2f),
            color = MaterialTheme.colorScheme.secondary,
        )
    }
}


fun getCodeChallengeTablePairs(
    codeChallengeDetail: CodeChallengeDetail, context: Context
): List<Pair<String, String?>> {
    return with(codeChallengeDetail) {
        listOf(
            Pair(context.getString(R.string.category), category),
            Pair(context.getString(R.string.rank), rank?.name),
            Pair(context.getString(R.string.total_attempts), totalAttempts.toString()),
            Pair(context.getString(R.string.total_completed), totalCompleted.toString()),
            Pair(context.getString(R.string.total_stars), totalStars.toString()),
            Pair(context.getString(R.string.total_score), voteScore.toString()),
            Pair(context.getString(R.string.created_by), createdBy?.username),
            Pair(context.getString(R.string.approved_by), approvedBy?.username),
            Pair(context.getString(R.string.published_at), publishedAt),
            Pair(context.getString(R.string.approved_at), approvedAt)
        )
    }
}

@DevicePreviews
@Composable
fun CodeChallengeContentPreview() {
    AppTheme {
        CodeChallengeContent(
            codeChallengeDetail = CodeChallengeDetail(
                "",
                "Range Extraction",
                "",
                "",
                "algorithms",
                "Write a function called `validBraces` that takes a string ...",
                listOf("Algorithms", "Validation", "Logic", "Utilities"),
                listOf("javascript", "coffeescript"),
                Rank(name = "4 kyu"),
                CreatedBy(username = "username"),
                ApprovedBy(username = "username"),
                100,
                100,
                50,
                50,
                "2013-11-05",
                "2013-11-05"
            ), context = LocalContext.current
        )
    }
}

@BackgroundPreview
@Composable
fun TitlePreview() {
    AppTheme {
        Title("The builder of things")
    }
}

@BackgroundPreview
@Composable
fun DescriptionPreview() {
    AppTheme {
        Description(
            "Write a function called `validBraces` that takes a string ..."
        )
    }
}

@BackgroundPreview
@Composable
fun CodeChallengeRowItemPreview() {
    AppTheme {
        CodeChallengeRowItem(
            stringResource(id = R.string.category),
            "some category"
        )
    }
}

@BackgroundPreview
@Composable
fun CodeChallengeTagsItemPreview() {
    AppTheme {
        CodeChallengeTagsItem(
            stringResource(id = R.string.languages),
            listOf("kotlin", "javascript", "python")
        )
    }
}
