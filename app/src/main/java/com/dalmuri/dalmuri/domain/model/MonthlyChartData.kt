package com.dalmuri.dalmuri.domain.model

import java.time.YearMonth

data class MonthlyChartData(
    val yearMonth: YearMonth,
    val emotionCounts: Map<Emotion, Int>, // Emotion to count
    val dailyEmotionScores: List<DailyEmotion>,
    val totalTilCount: Int,
    val averageEmotionScore: Float,
) {
    data class DailyEmotion(
        val timestamp: Long,
        val emotion: Emotion,
    )
}
