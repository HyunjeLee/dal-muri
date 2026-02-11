package com.dalmuri.dalmuri.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dalmuri.dalmuri.domain.model.Til
import com.dalmuri.dalmuri.presentation.theme.DalmuriTheme
import com.dalmuri.dalmuri.presentation.utils.toFormattedDate

@Composable
fun AiAnalysisCard(til: Til) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Emotion Emoji Circle
            Box(
                modifier =
                    Modifier
                        .size(48.dp)
                        .background(
                            color = MaterialTheme.colorScheme.onTertiary,
                            shape = CircleShape,
                        ),
                contentAlignment = Alignment.Center,
            ) { Text(text = til.emoji, fontSize = 16.sp) }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = til.emotion ?: "AI 분석 실패",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.padding(2.dp))
                Text(
                    text = til.createdAt.toFormattedDate() + " 학습 요약",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = "학습 난이도",
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = til.difficultyLevel ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Text(
                text = til.aiComment ?: "",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Preview
@Composable
private fun AiAnalysisCardPreview() {
    DalmuriTheme {
        AiAnalysisCard(
            Til(
                title = "test-title",
                learned = "learned~~~",
                createdAt = System.currentTimeMillis(),
                emotion = "최고의 하루 !",
                emotionScore = 5,
                difficultyLevel = "어려움",
                aiComment = "“\uD83C\uDF7B 목표를 완벽하게 달성하셨네요! 이 기세를 몰아 내일은 더 큰 성장에 도전해 보세요.”",
            ),
        )
    }
}
