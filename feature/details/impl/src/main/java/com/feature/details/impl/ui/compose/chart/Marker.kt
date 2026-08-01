package com.feature.details.impl.ui.compose.chart

import android.text.Layout
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisGuidelineComponent
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.common.component.fixed
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.component.shadow
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.insets
import com.patrykandpatrick.vico.compose.common.shape.markerCorneredShape
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker.ValueFormatter
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.shape.CorneredShape.Corner.Relative
import com.patrykandpatrick.vico.core.common.shape.CorneredShape.CornerTreatment

private const val LABEL_BACKGROUND_SHADOW_RADIUS_DP = 4f
private const val LABEL_BACKGROUND_SHADOW_DY_DP = 2f

@Composable
fun rememberMarker(valueFormatter: ValueFormatter): CartesianMarker {
    val labelBackgroundShape =
        markerCorneredShape(Relative(sizePercent = 15, treatment = CornerTreatment.Rounded))
    val labelBackground = rememberShapeComponent(
        fill = fill(MaterialTheme.colorScheme.surfaceContainer),
        shape = labelBackgroundShape,
        shadow = shadow(
            radius = LABEL_BACKGROUND_SHADOW_RADIUS_DP.dp,
            y = LABEL_BACKGROUND_SHADOW_DY_DP.dp
        ),
    )
    val label = rememberTextComponent(
        color = MaterialTheme.colorScheme.onSurface,
        lineCount = 5,
        textAlignment = Layout.Alignment.ALIGN_CENTER,
        padding = insets(8.dp, 4.dp),
        background = labelBackground,
        minWidth = TextComponent.MinWidth.fixed(40.dp),
    )
    val guideline = rememberAxisGuidelineComponent()
    return rememberDefaultCartesianMarker(
        label = label,
        valueFormatter = valueFormatter,
        guideline = guideline,
        labelPosition = DefaultCartesianMarker.LabelPosition.AroundPoint,
    )
}
