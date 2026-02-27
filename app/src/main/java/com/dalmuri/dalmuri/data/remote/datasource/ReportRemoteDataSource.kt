package com.dalmuri.dalmuri.data.remote.datasource

import com.dalmuri.dalmuri.data.remote.model.MonthlyReviewDto
import com.dalmuri.dalmuri.domain.model.Emotion

interface ReportRemoteDataSource {
    suspend fun generateChartSummary(
        totalTilCount: Int,
        averageEmotionScore: Float,
        emotionCounts: Map<Emotion, Int>,
    ): String

    suspend fun generateMonthlyReview(
        totalCount: Int,
        averageEmotionScore: Float,
        emotionDistribution: String,
        tilDetails: String,
    ): MonthlyReviewDto
}
