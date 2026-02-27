package com.dalmuri.dalmuri.presentation.review.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.dalmuri.dalmuri.domain.model.MonthlyReview
import com.dalmuri.dalmuri.presentation.review.ReviewContent
import com.dalmuri.dalmuri.presentation.review.ReviewContract
import com.dalmuri.dalmuri.presentation.theme.DalmuriTheme

@Preview(showBackground = true)
@Composable
fun ReviewScreenPreview(
    @PreviewParameter(ReviewStateProvider::class) state: ReviewContract.State,
) {
    DalmuriTheme {
        ReviewContent(
            state = state,
            onMonthChange = {},
        )
    }
}

class ReviewStateProvider : PreviewParameterProvider<ReviewContract.State> {
    override val values =
        sequenceOf(
            ReviewContract.State(isLoading = true), // UI 로딩 중
            ReviewContract.State(isLoading = false, isAiAnalysisLoading = true), // AI 로딩 중
            ReviewContract.State( // 데이터 로드 완료
                reviewData =
                    MonthlyReview(
                        keywords =
                            listOf(
                                "Kotlin",
                                "Compose",
                                "Room",
                                "Hilt",
                                "Retrofit",
                                "OkHttp",
                                "Coroutines",
                            ),
                        overallMood = "Excellent &&&&&&&&&&&&& (75%)",
                        challengeDate = "2026.02.14",
                        growthPoints =
                            listOf(
                                "Completed Kotlin Advanced Course",
                                "Built and deployed first Jetpack Compose app screen using MVI pattern",
                                "Refined UI design skills with Material 3 guidelines and custom components",
                                "Improved code readability by 20% through refactoring",
                            ),
                        nextMonthAdvice =
                            "Focus on integrating state management with Compose. Try building a small project to practice real-world implementation. Don't forget to take regular breaks to manage stress.",
                    ),
            ),
        )
}
