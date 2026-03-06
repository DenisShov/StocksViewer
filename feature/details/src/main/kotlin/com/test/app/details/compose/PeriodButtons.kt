package com.test.app.details.compose

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.test.app.commonresources.R
import com.test.app.designsystem.component.BackgroundPreview
import com.test.app.designsystem.theme.AppTheme

@Composable
fun PeriodButtons(onChartPeriodChange: (String) -> Unit) {
    var selectedPeriod by rememberSaveable { mutableStateOf("week") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        PeriodButton(
            text = stringResource(R.string.day),
            isSelected = selectedPeriod == "day",
            onClick = {
                selectedPeriod = "day"
                onChartPeriodChange("day")
            },
            modifier = Modifier.weight(1f)
        )
        PeriodButton(
            text = stringResource(R.string.week),
            isSelected = selectedPeriod == "week",
            onClick = {
                selectedPeriod = "week"
                onChartPeriodChange("week")
            },
            modifier = Modifier.weight(1f)
        )
        PeriodButton(
            text = stringResource(R.string.month),
            isSelected = selectedPeriod == "month",
            onClick = {
                selectedPeriod = "month"
                onChartPeriodChange("month")
            },
            modifier = Modifier.weight(1f)
        )
        PeriodButton(
            text = stringResource(R.string.quartal),
            isSelected = selectedPeriod == "quarter",
            onClick = {
                selectedPeriod = "quarter"
                onChartPeriodChange("quarter")
            },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun PeriodButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
        animationSpec = tween(300),
        label = "backgroundColor"
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
        animationSpec = tween(300),
        label = "textColor"
    )
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@BackgroundPreview
@Composable
fun PeriodButtonsPreview() {
    AppTheme {
        PeriodButtons(onChartPeriodChange = {})
    }
}