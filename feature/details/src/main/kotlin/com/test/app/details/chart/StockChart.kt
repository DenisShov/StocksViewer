package com.test.app.details.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberCandlestickCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.core.cartesian.Scroll
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.candlestickSeries
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import com.test.app.details.model.CandleUiModel
import kotlinx.coroutines.runBlocking
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.ceil
import kotlin.math.floor

private const val Y_STEP = 10.0
private val StartAxisValueFormatter = CartesianValueFormatter.decimal(DecimalFormat("$#,###"))
private val StartAxisItemPlacer = VerticalAxis.ItemPlacer.step({ Y_STEP })

private val RangeProvider = object : CartesianLayerRangeProvider {
    override fun getMinY(minY: Double, maxY: Double, extraStore: ExtraStore) =
        Y_STEP * floor(minY / Y_STEP)

    override fun getMaxY(minY: Double, maxY: Double, extraStore: ExtraStore) =
        Y_STEP * ceil(maxY / Y_STEP)
}

@Composable
private fun StockChartContent(
    modelProducer: CartesianChartModelProducer,
    time: List<Long>,
    modifier: Modifier = Modifier,
    axisLabelKey: ExtraStore.Key<Map<Int, String>> = ExtraStore.Key(),
) {
    val chart = rememberCartesianChart(
        rememberCandlestickCartesianLayer(rangeProvider = RangeProvider),
        startAxis = VerticalAxis.rememberStart(
            valueFormatter = StartAxisValueFormatter,
            itemPlacer = StartAxisItemPlacer,
        ),
        bottomAxis = HorizontalAxis.rememberBottom(
            itemPlacer = remember(time) {
                HorizontalAxis.ItemPlacer.aligned(
                    spacing = { 2 },
                    addExtremeLabelPadding = true
                )
            },
            guideline = null,
            valueFormatter = remember(time) {
                CartesianValueFormatter { _, value, _ ->
                    val timestamp = time.getOrNull(value.toInt())
                    if (timestamp != null) {
                        formateDate(timestamp)
                    } else {
                        "N/A"
                    }
                }
            },
        ),
        marker = rememberMarker(
            valueFormatter = DefaultCartesianMarker.ValueFormatter { context, targets ->
                val x = targets.firstOrNull()?.x?.toInt() ?: return@ValueFormatter ""
                val label = context.model.extraStore[axisLabelKey].get(x)
                label ?: ""
            }
        ),
    )
    val scrollState = rememberVicoScrollState(
        initialScroll = Scroll.Absolute.End // Scroll to the end
    )
    CartesianChartHost(
        chart = chart,
        modelProducer = modelProducer,
        scrollState = scrollState,
        modifier = modifier.height(600.dp),
    )
}

private fun getBottomAxisValueFormatter(time: List<Long>): CartesianValueFormatter =
    CartesianValueFormatter { _, value, _ ->
//        val timestamp = time.getOrNull(value.toInt())
//        if (timestamp != null) {
//            formateDate(timestamp)
//        } else {
//            "EMPTY"
//        }
        time.getOrNull(value.toInt())
            ?.let { timestamp -> formateDate(timestamp) }
            .orEmpty()
    }

@Composable
fun StockChart(
    modifier: Modifier = Modifier,
    data: List<CandleUiModel>,
) {
    val time = data.map { it.timestampMs }
    val open = data.map { it.open }
    val close = data.map { it.close }
    val low = data.map { it.low }
    val high = data.map { it.high }

    val axisLabelKey = ExtraStore.Key<Map<Int, String>>()
    val axisLabels = remember(data) { getAxisLabels(data) }

    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(data) {
        modelProducer.runTransaction {
            candlestickSeries(
                opening = open,
                closing = close,
                low = low,
                high = high
            )
            extras { it[axisLabelKey] = axisLabels }
        }
    }
    StockChartContent(
        modelProducer = modelProducer,
        time = time,
        modifier = modifier,
        axisLabelKey = axisLabelKey,
    )
}

private fun getAxisLabels(data: List<CandleUiModel>): MutableMap<Int, String> {
    val axisLabels = mutableMapOf<Int, String>()
    data.forEachIndexed { idx, data ->
        val dateStr = formateDate(data.timestampMs)
        axisLabels[idx] = "Date: $dateStr\n" +
                "Open: $${data.open}\n" +
                "Close: $${data.close}\n" +
                "Low: $${data.low}\n" +
                "High: $${data.high}"
    }
    return axisLabels
}

private fun formateDate(millis: Long) = SimpleDateFormat("MMM dd yyyy", Locale.US).format(millis)

@Composable
@Preview
private fun Preview() {
    val modelProducer = remember { CartesianChartModelProducer() }
    // Use `runBlocking` only for previews, which don’t support asynchronous execution.
    runBlocking {
        modelProducer.runTransaction {
            candlestickSeries(opening, closing, low, high)
        }
    }
    Box(
        modifier = Modifier
            .background(Color.White)
            .padding(16.dp)
    ) {
        StockChartContent(
            modelProducer = modelProducer,
            time = time,
            modifier = Modifier
        )
    }
}

private val time = listOf(
    1699851600000,
    1699938000000,
    1700024400000,
    1700110800000,
    1700197200000,
    1700456400000,
    1700542800000,
    1700629200000,
    1700802000000,
    1701061200000,
    1701147600000,
    1701234000000,
    1701320400000,
    1701406800000,
    1701666000000,
    1701752400000,
    1701838800000,
    1701925200000,
    1702011600000,
    1702270800000,
    1702357200000,
)

private val opening = listOf<Number>(
    185.82,
    187.7,
    187.845,
    189.57,
    190.25,
    189.89,
    191.41,
    191.49,
    190.87,
    189.92,
    189.78,
    190.9,
    189.84,
    190.33,
    189.98,
    190.21,
    194.45,
    193.63,
    194.2,
    193.11,
    193.08,
)

private val closing = listOf<Number>(
    184.8,
    187.44,
    188.01,
    189.71,
    189.69,
    191.45,
    190.64,
    191.31,
    189.97,
    189.79,
    190.4,
    189.37,
    189.95,
    191.24,
    189.43,
    193.42,
    192.32,
    194.27,
    195.71,
    193.18,
    194.71,
)

private val low = listOf<Number>(
    184.21,
    186.3,
    187.78,
    188.65,
    188.57,
    189.88,
    189.74,
    190.825,
    189.25,
    188.9,
    189.4,
    188.97,
    188.19,
    189.23,
    187.4511,
    190.18,
    192.11,
    193.59,
    193.67,
    191.42,
    191.721,
)

private val high = listOf<Number>(
    186.03,
    188.11,
    189.5,
    190.96,
    190.38,
    191.905,
    191.52,
    192.93,
    190.9,
    190.67,
    191.08,
    192.09,
    190.32,
    191.56,
    190.05,
    194.4,
    194.76,
    195.0,
    195.99,
    193.49,
    194.72,
)
