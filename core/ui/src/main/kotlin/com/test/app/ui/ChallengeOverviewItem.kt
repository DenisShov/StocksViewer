package com.test.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.test.app.designsystem.component.ThemePreviews
import com.test.app.designsystem.theme.AppTheme
import com.test.app.model.data.CodeChallengeOverview

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
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
            )
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                items(count = challengeOverview.completedLanguages.size) { index ->
                    Text(
                        modifier = Modifier
                            .padding(vertical = 8.dp, horizontal = 4.dp)
                            .background(
                                color = MaterialTheme.colorScheme.background,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        text = challengeOverview.completedLanguages[index],
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            Text(
                text = challengeOverview.completedAt,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .align(Alignment.End)
                    .wrapContentWidth()
                    .padding(8.dp)
            )
        }
    }
}

@ThemePreviews
@Composable
fun VideoItemPreview() {
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
