package com.dalmuri.dalmuri.domain.repository

import com.dalmuri.dalmuri.domain.model.Emotion

interface ReportRepository {
    suspend fun getChartSummary(
        totalTilCount: Int,
        averageEmotionScore: Float,
        emotionCounts: Map<Emotion, Int>, // Emotion to count
    ): Result<String>

    suspend fun saveMonthlyReport(
        yearMonth: String,
        chartSummary: String?,
    ): Result<Unit>
}
