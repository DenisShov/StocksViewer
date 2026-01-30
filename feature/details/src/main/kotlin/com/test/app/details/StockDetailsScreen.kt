package com.test.app.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.test.app.common.error.DomainError
import com.test.app.data.util.toErrorMessage
import com.test.app.designsystem.component.BackgroundPreview
import com.test.app.designsystem.component.LoadingData
import com.test.app.designsystem.theme.AppTheme
import com.test.app.details.actions.StockDetailsActions
import com.test.app.details.chart.StockChart
import com.test.app.details.model.CandleUiModel
import com.test.app.details.model.StockOverviewUiModel
import com.test.app.ui.showSnackBar

@Composable
fun StockDetailsRoute(
    viewModel: StockDetailsViewModel,
    onBackButtonClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val actions = StockDetailsActions(
        onChartPeriodChange = viewModel::getStockChartData
    )

    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val retry = stringResource(com.test.app.commonresources.R.string.retry)

    StockDetailsScreen(
        uiState = uiState,
        snackBarHostState = snackBarHostState,
        onBackButtonClick = onBackButtonClick,
        onShowErrorSnackbar = { appError ->
            showSnackBar(
                scope = scope,
                snackBarHostState = snackBarHostState,
                message = appError.toErrorMessage(context),
                actionLabel = retry,
                actionPerformed = { viewModel.getStockOverviewByTicker() },
                dismissed = {},
            )
        },
        actions = actions,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockDetailsScreen(
    uiState: StockDetailsViewModel.State,
    snackBarHostState: SnackbarHostState,
    onBackButtonClick: () -> Unit = {},
    onShowErrorSnackbar: (DomainError) -> Unit = {},
    actions: StockDetailsActions,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = {}, navigationIcon = {
                IconButton(onClick = { onBackButtonClick.invoke() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                        candles = stockDetailsUiState.candles,
                        actions = actions,
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
private fun StockDetailsContent(
    stockOverview: StockOverviewUiModel,
    candles: List<CandleUiModel>,
    actions: StockDetailsActions,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(8.dp),
    ) {
        stockOverview.iconUrl?.let {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AsyncImage(
                    modifier = Modifier.size(48.dp),
                    model = it,
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    contentDescription = "",
                )
            }
        }

        Title(stockOverview.name)
        Description(stockOverview)

        if (candles.isNotEmpty()) {
            StockChart(
                modifier = Modifier,
                data = candles,
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Button(
                onClick = { actions.onChartPeriodChange("day") }
            ) {
                Text(
                    text = "Day",
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            Button(
                onClick = { actions.onChartPeriodChange("week") }
            ) {
                Text(
                    text = "Week",
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            Button(
                onClick = { actions.onChartPeriodChange("month") }
            ) {
                Text(
                    text = "Month",
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            Button(
                onClick = { actions.onChartPeriodChange("quarter") }
            ) {
                Text(
                    text = "Quarter",
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
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
private fun Description(stockOverview: StockOverviewUiModel) {
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

        stockOverview.description?.let {
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = it,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        stockOverview.homepageUrl?.let {
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = it,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = "Locale: ${stockOverview.locale}",
            style = MaterialTheme.typography.bodyMedium,
        )

        stockOverview.totalEmployees?.let {
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = "Total employees: $it",
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        stockOverview.listDate?.let {
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = "List date: $it",
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
}

@BackgroundPreview
@Composable
fun StockDetailsContentPreview() {
    AppTheme {
        StockDetailsContent(
            stockOverview = StockOverviewUiModel(
                ticker = "",
                name = "",
                locale = "",
                type = "",
                currencyName = null,
                marketCap = null,
                description = null,
                homepageUrl = null,
                totalEmployees = null,
                sicDescription = null,
                listDate = null,
                iconUrl = null,
            ),
            candles = emptyList(),
            actions = StockDetailsActions(
                onChartPeriodChange = {}
            ),
        )
    }
}

@BackgroundPreview
@Composable
fun TitlePreview() {
    AppTheme {
        Title(title = "Agilent Technologies Inc.")
    }
}

@BackgroundPreview
@Composable
fun DescriptionPreview() {
    AppTheme {
        Description(
            stockOverview = StockOverviewUiModel(
                ticker = "Ticker",
                name = "Name",
                locale = "Locale",
                type = "Type",
                description = "Originally spun out of Hewlett-Packard in 1999, " +
                        "Agilent has evolved into a leading life science and diagnostic firm. " +
                        "Today, Agilent's measurement technologies serve a broad base of customers " +
                        "with its three operating segments: life science and applied tools, cross " +
                        "lab consisting of consumables and services related to life science and " +
                        "applied tools, and diagnostics and genomics.",
            ),
        )
    }
}
