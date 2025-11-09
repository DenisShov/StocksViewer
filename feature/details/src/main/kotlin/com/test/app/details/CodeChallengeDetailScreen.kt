package com.test.app.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.test.app.common.error.AppError
import com.test.app.common.result.toErrorMessage
import com.test.app.designsystem.component.BackgroundPreview
import com.test.app.designsystem.component.LoadingData
import com.test.app.designsystem.icon.CodeWarsIcon.ArrowBack
import com.test.app.designsystem.theme.AppTheme
import com.test.app.details.StockDetailsViewModel.Companion.STOCK_TICKER_ARG
import com.test.app.model.data.StockChart
import com.test.app.model.data.StockOverview
import com.test.app.ui.showSnackBar
import dev.olshevski.navigation.reimagined.hilt.hiltViewModel

@Composable
fun StockDetailsRoute(
    stockTicker: String,
    viewModel: StockDetailsViewModel = hiltViewModel(
        defaultArguments = bundleOf(STOCK_TICKER_ARG to stockTicker)
    ),
    onBackButtonClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    StockDetailsScreen(
        uiState = uiState,
        snackBarHostState = snackBarHostState,
        onBackButtonClick = onBackButtonClick,
        onShowErrorSnackbar = { appError ->
            showSnackBar(
                scope = scope,
                snackBarHostState = snackBarHostState,
                message = appError.toErrorMessage(context),
                actionLabel = context.resources.getString(com.test.app.commonresources.R.string.retry),
                actionPerformed = { viewModel.getStockOverviewByTicker() },
                dismissed = {}
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockDetailsScreen(
    uiState: StockDetailsViewModel.State = StockDetailsViewModel.State(
        stockDetailsState = StockDetailsViewModel.StockDetailsState.Loading
    ),
    snackBarHostState: SnackbarHostState,
    onBackButtonClick: () -> Unit = {},
    onShowErrorSnackbar: (AppError) -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(title = {}, navigationIcon = {
                IconButton(onClick = { onBackButtonClick.invoke() }) {
                    Icon(
                        imageVector = ArrowBack,
                        contentDescription = stringResource(id = com.test.app.commonresources.R.string.ui_return_to_previous_screen),
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
            when (val stockDetailsUiState = uiState.stockDetailsState) {
                is StockDetailsViewModel.StockDetailsState.Loading -> {
                    LoadingData(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is StockDetailsViewModel.StockDetailsState.Success -> {
                    StockDetailsContent(
                        stockOverview = stockDetailsUiState.stockOverview,
                        stockChart = stockDetailsUiState.stockChart,
                    )
                }

                is StockDetailsViewModel.StockDetailsState.Error -> {
                    Text(
                        text = stringResource(id = com.test.app.commonresources.R.string.some_error_happened),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(align = Alignment.CenterVertically)
                            .padding(16.dp),
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                    )
                    onShowErrorSnackbar.invoke(stockDetailsUiState.error)
                }
            }
        }
    }
}

@Composable
private fun StockDetailsContent(stockOverview: StockOverview, stockChart: StockChart) {
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
            stockOverview.results.name.let {
                Title(it)
            }
            stockOverview.results.description?.let {
                Description(it)
            }

//            StockChartGraph(
//                modifier = Modifier,
//                opening = getOpening(stockChart),
//                closing = getClosing(stockChart),
//                low = getLow(stockChart),
//                high = getHigh(stockChart),
//            )
        }
    }
}

//opening: Collection<Number>,
//               closing: Collection<Number>,
//               low: Collection<Number>,
//               high: Collection<Number>,

private fun getOpening(stockChart: StockChart): Collection<Number> {
    return stockChart.results.map { it.open }.toList()
}

private fun getClosing(stockChart: StockChart): Collection<Number> {
    return stockChart.results.map { it.close }.toList()
}

private fun getLow(stockChart: StockChart): Collection<Number> {
    return stockChart.results.map { it.low }.toList()
}

private fun getHigh(stockChart: StockChart): Collection<Number> {
    return stockChart.results.map { it.high }.toList()
}

@Composable
private fun Title(title: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
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
            text = stringResource(id = com.test.app.commonresources.R.string.description),
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

//@DevicePreviews
//@Composable
//fun CodeChallengeContentPreview() {
//    AppTheme {
//        StockDetailsContent(
//            stockDetails = StockDetails(
//                "",
//                "Range Extraction",
//                "",
//                "",
//                "algorithms",
//                "Write a function called `validBraces` that takes a string ...",
//                listOf("Algorithms", "Validation", "Logic", "Utilities"),
//                listOf("javascript", "coffeescript"),
//                Rank(name = "4 kyu"),
//                CreatedBy(username = "username"),
//                ApprovedBy(username = "username"),
//                100,
//                50,
//                50,
//                50,
//                "2013-11-05",
//                "2013-11-05"
//            )
//        )
//    }
//}

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

//@BackgroundPreview
//@Composable
//fun CodeChallengeRowItemPreview() {
//    AppTheme {
//        CodeChallengeRowItem(
//            stringResource(id = com.test.app.commonresources.R.string.category),
//            "some category"
//        )
//    }
//}
//
//@BackgroundPreview
//@Composable
//fun CodeChallengeTagsItemPreview() {
//    AppTheme {
//        CodeChallengeTagsItem(
//            stringResource(id = com.test.app.commonresources.R.string.languages),
//            listOf("kotlin", "javascript", "python")
//        )
//    }
//}
