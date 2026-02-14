package com.test.app.details

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.test.app.common.error.DomainError
import com.test.app.data.util.toErrorMessage
import com.test.app.designsystem.component.BackgroundPreview
import com.test.app.designsystem.component.LoadingData
import com.test.app.designsystem.theme.AppTheme
import com.test.app.details.actions.StockDetailsActions
import com.test.app.details.chart.StockChart
import com.test.app.details.model.CandleUiModel
import com.test.app.details.model.StockOverviewUiModel
import com.test.app.details.state.StockDetailsState
import androidx.compose.ui.draw.clip
import com.test.app.ui.showSnackBar
import java.text.NumberFormat
import java.util.Locale

@Composable
fun StockDetailsRoute2(
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

    StockDetailScreen(
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
fun StockDetailScreen(
    uiState: StockDetailsState,
    snackBarHostState: SnackbarHostState,
    onBackButtonClick: () -> Unit = {},
    onShowErrorSnackbar: (DomainError) -> Unit = {},
    actions: StockDetailsActions,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.stockOverview?.ticker.orEmpty(),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBackButtonClick.invoke() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = com.test.app.commonresources.R.string.ui_return_to_previous_screen),
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
        snackbarHost = { SnackbarHost(snackBarHostState) },
        contentWindowInsets = WindowInsets.navigationBars,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading -> {
                    LoadingData(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                uiState.stockOverview != null && uiState.candles.isNotEmpty() -> {
                    StockDetailsContent2(
                        stockOverview = uiState.stockOverview,
                        candles = uiState.candles,
                        actions = actions,
                    )
                }

                uiState.error != null -> {
                    Text(
                        text = stringResource(id = com.test.app.commonresources.R.string.some_error_happened),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(align = Alignment.CenterVertically)
                            .padding(16.dp),
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                    )
                    onShowErrorSnackbar.invoke(uiState.error)
                }
            }
        }
    }
}

@Composable
fun StockDetailsContent2(
    stockOverview: StockOverviewUiModel,
    candles: List<CandleUiModel>,
    actions: StockDetailsActions,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
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

            Chart(candles = candles, actions = actions)
        }
    }
}

@Composable
fun CompanyHeader(stock: StockOverviewUiModel) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        val logoString = stringResource(com.test.app.commonresources.R.string.logo_string)
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(stock.iconUrl)
                .crossfade(true)
                .build(),
            contentDescription = "${stock.name} $logoString",
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape),
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
fun KeyStatsGrid(stock: StockOverviewUiModel) {
    Column {
        if (stock.marketCap != null && stock.totalEmployees != null ||
            stock.listDate != null && stock.sicDescription != null
        ) {
            Text(
                text = stringResource(com.test.app.commonresources.R.string.market_data),
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
                    label = stringResource(com.test.app.commonresources.R.string.market_cap),
                    value = "$${formatCompactNumber(stock.marketCap)}"
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(com.test.app.commonresources.R.string.employees),
                    value = NumberFormat.getNumberInstance(Locale.US).format(stock.totalEmployees)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
        if (stock.listDate != null && stock.sicDescription != null) {
            Column (
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    label = stringResource(com.test.app.commonresources.R.string.sector),
                    value = stock.sicDescription,
                )
                StatCard(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(com.test.app.commonresources.R.string.listed_date),
                    value = stock.listDate,
                )
            }
        }
    }
}

fun formatCompactNumber(number: Double): String {
    return when {
        number >= 1_000_000_000_000 -> "%.2fT".format(number / 1_000_000_000_000)
        number >= 1_000_000_000 -> "%.2fB".format(number / 1_000_000_000)
        number >= 1_000_000 -> "%.2fM".format(number / 1_000_000)
        number >= 1_000 -> "%.2fK".format(number / 1_000)
        else -> "%.2f".format(number)
    }
}

@Composable
fun StatCard(modifier: Modifier = Modifier, label: String, value: String) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
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
fun CompanyAbout(description: String) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(
            text = "About",
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
                    text = if (expanded) stringResource(com.test.app.commonresources.R.string.show_less) else
                        stringResource(com.test.app.commonresources.R.string.read_more),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
fun ContactInfo(stock: StockOverviewUiModel) {
    Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
        if (stock.address.isNullOrEmpty().not()) {
            ContactRow(icon = Icons.Default.LocationOn, text = stock.address)
        }
        if (stock.homepageUrl.isNullOrEmpty().not()) {
            ContactRow(icon = Icons.Default.Language, text = stock.homepageUrl)
        }
        if (stock.cik.isNullOrEmpty().not()) {
            ContactRow(
                icon = Icons.Outlined.Info,
                text = stringResource(com.test.app.commonresources.R.string.cik) + " ${stock.cik}"
            )
        }
    }
}

@Composable
fun ContactRow(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector = icon,
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

//// --- 4. Android Studio Preview ---
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun StockDetailScreenPreview() {
//    // Mock Data based on your JSON
//    val mockAdobe = StockData(
//        ticker = "ADBE",
//        name = "Adobe Inc.",
//        marketCap = 1.10584595e11, // approx 110.58 Billion
//        description = "Adobe provides content creation, document management, and digital marketing and advertising software and services to creative professionals and marketers for creating, managing, delivering, measuring, optimizing, and engaging with compelling content multiple operating systems, devices, and media.",
//        logoUrl = "", // Empty for preview (Coil won't load in standard preview without mock engine)
//        employees = 31360,
//        listDate = "1986-08-20",
//        exchange = "NASDAQ",
//        homepage = "https://www.adobe.com",
//        address = "345 Park Ave, San Jose, CA"
//    )
//
//    MaterialTheme {
//        StockDetailScreen(stock = mockAdobe)
//    }
//}

@Composable
private fun Chart(candles: List<CandleUiModel>, actions: StockDetailsActions) {
    if (candles.isNotEmpty()) {
        StockChart(
            modifier = Modifier,
            data = candles,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Button(
                onClick = { actions.onChartPeriodChange("day") }
            ) {
                Text(
                    text = stringResource(com.test.app.commonresources.R.string.day),
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            Button(
                onClick = { actions.onChartPeriodChange("week") }
            ) {
                Text(
                    text = stringResource(com.test.app.commonresources.R.string.week),
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            Button(
                onClick = { actions.onChartPeriodChange("month") }
            ) {
                Text(
                    text = stringResource(com.test.app.commonresources.R.string.month),
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            Button(
                onClick = { actions.onChartPeriodChange("quarter") }
            ) {
                Text(
                    text = stringResource(com.test.app.commonresources.R.string.quartal),
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
                exchange = "Exchange",
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
