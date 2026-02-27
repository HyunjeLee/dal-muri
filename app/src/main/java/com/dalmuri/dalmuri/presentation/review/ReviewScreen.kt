package com.dalmuri.dalmuri.presentation.review

import android.R.attr.maxLines
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dalmuri.dalmuri.domain.model.MonthlyReview
import com.patrykandpatrick.vico.compose.cartesian.axis.text
import kotlinx.coroutines.delay

@Composable
fun ReviewScreen(viewModel: ReviewViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    ReviewContent(
        state = uiState,
        onMonthChange = { delta ->
            viewModel.handleIntent(ReviewContract.Intent.ChangeMonth(delta))
        },
    )
}

@Composable
internal fun ReviewContent(
    state: ReviewContract.State,
    onMonthChange: (Int) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
    ) {
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.isAiAnalysisLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CustomLoadingText()
            }
        } else {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 32.dp)
                        .padding(bottom = 32.dp),
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // 월간 선택 헤더
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = { onMonthChange(-1) }) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Previous Month",
                        )
                    }
                    Text(
                        text = "${state.month}월 월간 회고",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                    IconButton(onClick = { onMonthChange(1) }) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Next Month",
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                val review = state.reviewData ?: MonthlyReview()

                // TIL Keywords 섹션
                KeywordsSection(review.keywords)

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    thickness = 0.5.dp,
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Emotion Analysis 섹션
                Text(
                    text = "Emotion Analysis",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(16.dp))
                EmotionSection(review.overallMood, review.challengeDate)

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    thickness = 0.5.dp,
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Growth Point 섹션
                Text(
                    text = "Growth Point",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(16.dp))
                GrowthPointSection(review.growthPoints)

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    thickness = 0.5.dp,
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Advice 섹션
                AdviceSection(review.nextMonthAdvice)

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun KeywordsSection(keywords: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = "TIL Keywords",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                keywords.forEach { keyword ->
                    Box(
                        modifier =
                            Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    MaterialTheme.colorScheme.secondaryContainer,
                                ).padding(horizontal = 12.dp, vertical = 6.dp),
                    ) {
                        Text(
                            text = "#$keyword",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
                if (keywords.isEmpty()) {
                    Text(
                        text = "키워드가 없습니다.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline,
                    )
                }
            }
        }
    }
}

@Composable
private fun EmotionSection(
    overallMood: String,
    challengeDate: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        EmotionInfoItem(
            modifier = Modifier.weight(1.0f),
            emoji = "😎",
            label = "Overall Mood",
            value = overallMood.ifEmpty { "N/A" },
        )
        EmotionInfoItem(
            modifier = Modifier.weight(1.0f),
            emoji = "😰",
            label = "Challenge Mood",
            value = challengeDate.ifEmpty { "N/A" },
        )
    }
}

@Composable
private fun EmotionInfoItem(
    modifier: Modifier = Modifier,
    emoji: String,
    label: String,
    value: String,
) {
    Row(
        modifier = modifier.height(64.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.tertiaryContainer),
            contentAlignment = Alignment.Center,
        ) { Text(text = emoji, fontSize = 24.sp) }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
                fontWeight = FontWeight.Bold,
            )
            Text(
                modifier =
                    Modifier.basicMarquee(
                        iterations = Int.MAX_VALUE,
                    ),
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun GrowthPointSection(points: List<String>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        points.forEach { point ->
            Row(
                verticalAlignment = Alignment.Top,
            ) {
                // 아이콘을 텍스트 첫 줄 높이에 맞추기 위해 텍스트 스타일과 패딩 조절 가능
                Icon(
                    Icons.Default.Done,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = point,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
        if (points.isEmpty()) {
            Text("기록된 성장 포인트가 없습니다.", color = MaterialTheme.colorScheme.outline)
        }
    }
}

@Composable
private fun AdviceSection(advice: String) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.outline,
                    RoundedCornerShape(16.dp),
                ).padding(24.dp),
    ) {
        Column {
            Text(
                text = "Next Month Advice",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = advice.ifEmpty { "데이터를 분석 중이거나 조언이 없습니다." },
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 24.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}

@Composable
private fun CustomLoadingText() {
    // 2. 보여줄 메시지들을 리스트로 정의
    val messages =
        remember {
            listOf(
                "AI가 지난 한 달을 회고하고 있어요..",
                "키워드를 찾는 중이에요...",
                "감정 분석 중이에요...",
                "성장 포인트를 찾는 중이에요...",
                "다음 달 조언을 생성 중이에요...",
            )
        }

    // 3. 현재 보여줄 메시지의 인덱스 관리
    var currentIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(2000L)
            currentIndex = (currentIndex + 1) % messages.size
        }
    }
    Box(
        modifier = Modifier.fillMaxWidth().height(48.dp),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedContent(
            targetState = currentIndex,
            transitionSpec = {
                (slideInVertically { it } + fadeIn()) togetherWith (slideOutVertically { -it } + fadeOut())
            },
            label = "LoadingTextTransition",
            contentAlignment = Alignment.Center,
        ) { index ->
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = messages[index],
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}
