package com.feature.details.impl.compose

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.core.commonresources.R
import com.core.designsystem.component.BackgroundPreview
import com.core.designsystem.theme.AppTheme
import com.feature.details.impl.actions.ChartPeriod

@Composable
fun PeriodButtons(selectedPeriod: ChartPeriod, onChartPeriodChange: (ChartPeriod) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            .padding(4.dp)
            .testTag("period_buttons"),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        PeriodButton(
            text = stringResource(R.string.day),
            isSelected = selectedPeriod == ChartPeriod.DAY,
            onClick = {
                onChartPeriodChange(ChartPeriod.DAY)
            },
            modifier = Modifier
                .weight(1f)
                .testTag("period_button_day"),
        )
        PeriodButton(
            text = stringResource(R.string.week),
            isSelected = selectedPeriod == ChartPeriod.WEEK,
            onClick = {
                onChartPeriodChange(ChartPeriod.WEEK)
            },
            modifier = Modifier
                .weight(1f)
                .testTag("period_button_week"),
        )
        PeriodButton(
            text = stringResource(R.string.month),
            isSelected = selectedPeriod == ChartPeriod.MONTH,
            onClick = {
                onChartPeriodChange(ChartPeriod.MONTH)
            },
            modifier = Modifier
                .weight(1f)
                .testTag("period_button_month"),
        )
        PeriodButton(
            text = stringResource(R.string.quartal),
            isSelected = selectedPeriod == ChartPeriod.QUARTER,
            onClick = {
                onChartPeriodChange(ChartPeriod.QUARTER)
            },
            modifier = Modifier
                .weight(1f)
                .testTag("period_button_quarter"),
        )
    }
}

@Composable
private fun PeriodButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
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
        targetValue = if (isSelected) 1.05f else 1f, animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow
        ), label = "scale"
    )

    Box(modifier = modifier
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clip(RoundedCornerShape(8.dp))
        .background(backgroundColor)
        .clickable(onClick = onClick)
        .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center) {
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
        PeriodButtons(selectedPeriod = ChartPeriod.WEEK, onChartPeriodChange = {})
    }
}
