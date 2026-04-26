package com.feature.details.impl.ui.compose

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.core.commonresources.R
import com.core.designsystem.component.BackgroundPreview
import com.core.designsystem.component.HandleError
import com.core.designsystem.icon.IconResources
import com.core.designsystem.theme.AppTheme
import com.feature.details.impl.ui.StockDetailsViewModel
import com.feature.details.impl.ui.actions.ChartPeriod
import com.feature.details.impl.ui.actions.StockDetailsActions
import com.feature.details.impl.ui.compose.chart.CandleChart
import com.feature.details.impl.ui.model.CandleUiModel
import com.feature.details.impl.ui.model.StockOverviewUiModel
import com.feature.details.impl.ui.state.StockDetailsState
import java.text.NumberFormat
import java.util.Locale

@Composable
fun StockDetailsRoute(
    viewModel: StockDetailsViewModel,
    onBackButtonClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val actions = StockDetailsActions(
        onChartPeriodChange = viewModel::getStockChartData,
        retry = viewModel::getStockOverviewByTicker,
        retryChart = viewModel::retryGetStockChartData,
    )

    StockDetailScreen(
        uiState = uiState,
        onBackButtonClick = onBackButtonClick,
        actions = actions,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockDetailScreen(
    uiState: StockDetailsState,
    onBackButtonClick: () -> Unit = {},
    actions: StockDetailsActions,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.testTag("stock_details_ticker_name"),
                        text = uiState.stockOverview?.ticker.orEmpty(),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onBackButtonClick.invoke() },
                        modifier = Modifier.testTag("stock_details_back_button"),
                    ) {
                        Icon(
                            painter = painterResource(id = IconResources.ArrowBack),
                            contentDescription = stringResource(id = R.string.a11y_return_to_previous_screen),
                            modifier = Modifier.padding(start = 12.dp),
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                windowInsets = WindowInsets(),
            )
        },
        contentWindowInsets = WindowInsets.navigationBars,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading -> {
                    StockDetailsSkeleton()
                }

                uiState.stockOverview != null && uiState.candles.isNotEmpty() -> {
                    StockDetailsContent(
                        stockOverview = uiState.stockOverview,
                        candles = uiState.candles,
                        selectedPeriod = uiState.selectedPeriod,
                        isChartLoading = uiState.isChartLoading,
                        chartErrorString = uiState.chartErrorString,
                        actions = actions,
                    )
                }

                uiState.errorString != null -> {
                    HandleError(
                        errorMessage = uiState.errorString,
                        onRetry = actions.retry,
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("stock_details_error"),
                    )
                }
            }
        }
    }
}

@Composable
private fun StockDetailsContent(
    stockOverview: StockOverviewUiModel,
    candles: List<CandleUiModel>,
    selectedPeriod: ChartPeriod,
    isChartLoading: Boolean,
    chartErrorString: String?,
    actions: StockDetailsActions,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("stock_details_content"),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item { CompanyHeader(stock = stockOverview) }

        item { KeyStatsGrid(stock = stockOverview) }

        if (stockOverview.description.isNullOrEmpty().not()) {
            item {
                CompanyAbout(description = stockOverview.description)
            }
        }

        item {
            ContactInfo(stock = stockOverview)

            Chart(
                candles = candles,
                selectedPeriod = selectedPeriod,
                isChartLoading = isChartLoading,
                chartErrorString = chartErrorString,
                actions = actions,
            )
        }
    }
}

@Composable
private fun CompanyHeader(stock: StockOverviewUiModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.testTag("company_header"),
    ) {
        val logoString = stringResource(R.string.a11y_logo)
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(stock.iconUrl).crossfade(true)
                .build(),
            contentDescription = "${stock.name} $logoString",
            placeholder = painterResource(com.feature.details.impl.R.drawable.ic_image_placeholder),
            error = painterResource(com.feature.details.impl.R.drawable.ic_image_placeholder),
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .testTag("company_logo"),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = stock.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            SuggestionChip(
                onClick = { },
                label = { Text(stock.exchange) },
                modifier = Modifier.height(30.dp),
                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                border = null
            )
        }
    }
}

@Composable
private fun KeyStatsGrid(stock: StockOverviewUiModel) {
    Column(modifier = Modifier.testTag("key_stats_grid")) {
        if (stock.marketCap != null && stock.totalEmployees != null || stock.sicDescription != null) {
            Text(
                text = stringResource(R.string.market_data),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }
        if (stock.marketCap != null && stock.totalEmployees != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.market_cap),
                    value = stock.marketCap
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.employees),
                    value = NumberFormat.getNumberInstance(Locale.US).format(stock.totalEmployees)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
        if (stock.sicDescription != null) {
            StatCard(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(R.string.sector),
                value = stock.sicDescription,
            )
        }
    }
}

@Composable
private fun StatCard(modifier: Modifier = Modifier, label: String, value: String) {
    Card(
        modifier = modifier, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun CompanyAbout(description: String) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.testTag("about_section")) {
        Text(
            text = stringResource(R.string.about),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            onClick = { expanded = !expanded },
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    maxLines = if (expanded) Int.MAX_VALUE else 4,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 22.sp
                )
                Text(
                    text = if (expanded) {
                        stringResource(R.string.show_less)
                    } else {
                        stringResource(R.string.read_more)
                    },
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun ContactInfo(stock: StockOverviewUiModel) {
    Column(
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier.testTag("contact_info"),
    ) {
        if (stock.address.isNullOrEmpty().not()) {
            ContactRow(
                icon = IconResources.LocationOn, text = stock.address
            )
        }
        if (stock.homepageUrl.isNullOrEmpty().not()) {
            ContactRow(
                icon = IconResources.Language, text = stock.homepageUrl
            )
        }
        if (stock.listDate.isNullOrEmpty().not()) {
            ContactRow(
                icon = IconResources.CalendarToday, text = stock.listDate
            )
        }
        if (stock.cik.isNullOrEmpty().not()) {
            ContactRow(
                icon = IconResources.Info, text = stringResource(R.string.cik) + " ${stock.cik}"
            )
        }
    }
}

@Composable
private fun ContactRow(icon: Int, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun StockDetailsSkeleton() {
    val infiniteTransition = rememberInfiniteTransition(label = "skeleton")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 0.7f, animationSpec = infiniteRepeatable(
            animation = tween(1000), repeatMode = RepeatMode.Reverse
        ), label = "alpha"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("stock_details_loading_skeleton"),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha))
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Box(
                        modifier = Modifier
                            .width(150.dp)
                            .height(24.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(30.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha))
                    )
                }
            }
        }

        item {
            Column {
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha))
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    repeat(2) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(74.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha))
                        )
                    }

                }
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(74.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha))
                )
            }
        }

        item {
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha))
            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha))
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                repeat(4) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha))
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(16.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha))
                        )
                    }
                }
            }
        }

        item {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha))
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(4) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha))
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Chart(
    candles: List<CandleUiModel>,
    selectedPeriod: ChartPeriod,
    isChartLoading: Boolean,
    chartErrorString: String?,
    actions: StockDetailsActions,
) {
    when {
        isChartLoading -> {
            ChartLoading()
        }

        chartErrorString != null -> {
            HandleError(
                errorMessage = chartErrorString,
                onRetry = actions.retryChart,
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("stock_chart_details_error"),
            )
        }

        else -> {
            if (candles.isNotEmpty()) {
                Column(modifier = Modifier.testTag("chart_section")) {
                    CandleChart(
                        modifier = Modifier.testTag("stock_chart"),
                        data = candles,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    PeriodButtons(
                        selectedPeriod = selectedPeriod,
                        onChartPeriodChange = actions.onChartPeriodChange
                    )
                }
            }
        }
    }
}

@Composable
private fun ChartLoading() {
    val infiniteTransition = rememberInfiniteTransition(label = "skeleton")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 0.7f, animationSpec = infiniteRepeatable(
            animation = tween(1000), repeatMode = RepeatMode.Reverse
        ), label = "alpha"
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha))
    )
}

@BackgroundPreview
@Composable
private fun StockDetailsContentPreview() {
    AppTheme {
        StockDetailsContent(
            stockOverview = StockOverviewUiModel(
                ticker = "ADBE",
                name = "Adobe Inc.",
                exchange = "NASDAQ",
                marketCap = "110.59B",
                totalEmployees = 31360,
                sicDescription = "Technology",
                description = "Adobe provides content creation, document management, and digital marketing software.",
                address = "345 Park Ave, San Jose, CA",
                homepageUrl = "https://www.adobe.com",
                listDate = "20 August 1986",
                cik = "0000796343"
            ),
            candles = listOf(
                CandleUiModel(
                    open = 185.82,
                    close = 184.8,
                    high = 186.03,
                    low = 184.21,
                    timestampMs = 1699851600000
                ),
                CandleUiModel(
                    open = 187.7,
                    close = 187.44,
                    high = 188.11,
                    low = 186.3,
                    timestampMs = 1699938000000
                ),
                CandleUiModel(
                    open = 187.845,
                    close = 188.01,
                    high = 189.5,
                    low = 187.78,
                    timestampMs = 1700024400000
                ),
                CandleUiModel(
                    open = 189.57,
                    close = 189.71,
                    high = 190.96,
                    low = 188.65,
                    timestampMs = 1700110800000
                ),
                CandleUiModel(
                    open = 190.25,
                    close = 189.69,
                    high = 190.38,
                    low = 188.57,
                    timestampMs = 1700197200000
                ),
                CandleUiModel(
                    open = 189.89,
                    close = 191.45,
                    high = 191.905,
                    low = 189.88,
                    timestampMs = 1700456400000
                ),
                CandleUiModel(
                    open = 191.41,
                    close = 190.64,
                    high = 191.52,
                    low = 189.74,
                    timestampMs = 1700542800000
                ),
                CandleUiModel(
                    open = 191.49,
                    close = 191.31,
                    high = 192.93,
                    low = 190.825,
                    timestampMs = 1700629200000
                ),
                CandleUiModel(
                    open = 190.87,
                    close = 189.97,
                    high = 190.9,
                    low = 189.25,
                    timestampMs = 1700802000000
                ),
                CandleUiModel(
                    open = 189.92,
                    close = 189.79,
                    high = 190.67,
                    low = 188.9,
                    timestampMs = 1701061200000
                ),
                CandleUiModel(
                    open = 189.78,
                    close = 190.4,
                    high = 191.08,
                    low = 189.4,
                    timestampMs = 1701147600000
                ),
                CandleUiModel(
                    open = 190.9,
                    close = 189.37,
                    high = 192.09,
                    low = 188.97,
                    timestampMs = 1701234000000
                ),
                CandleUiModel(
                    open = 189.84,
                    close = 189.95,
                    high = 190.32,
                    low = 188.19,
                    timestampMs = 1701320400000
                ),
                CandleUiModel(
                    open = 190.33,
                    close = 191.24,
                    high = 191.56,
                    low = 189.23,
                    timestampMs = 1701406800000
                ),
                CandleUiModel(
                    open = 189.98,
                    close = 189.43,
                    high = 190.05,
                    low = 187.4511,
                    timestampMs = 1701666000000
                ),
                CandleUiModel(
                    open = 190.21,
                    close = 193.42,
                    high = 194.4,
                    low = 190.18,
                    timestampMs = 1701752400000
                ),
                CandleUiModel(
                    open = 194.45,
                    close = 192.32,
                    high = 194.76,
                    low = 192.11,
                    timestampMs = 1701838800000
                ),
                CandleUiModel(
                    open = 193.63,
                    close = 194.27,
                    high = 195.0,
                    low = 193.59,
                    timestampMs = 1701925200000
                ),
                CandleUiModel(
                    open = 194.2,
                    close = 195.71,
                    high = 195.99,
                    low = 193.67,
                    timestampMs = 1702011600000
                ),
                CandleUiModel(
                    open = 193.11,
                    close = 193.18,
                    high = 193.49,
                    low = 191.42,
                    timestampMs = 1702270800000
                ),
                CandleUiModel(
                    open = 193.08,
                    close = 194.71,
                    high = 194.72,
                    low = 191.721,
                    timestampMs = 1702357200000
                ),
            ),
            selectedPeriod = ChartPeriod.WEEK,
            isChartLoading = false,
            chartErrorString = null,
            actions = StockDetailsActions(),
        )
    }
}

@BackgroundPreview
@Composable
private fun StockDetailsSkeletonPreview() {
    AppTheme {
        StockDetailsSkeleton()
    }
}
