package com.dalmuri.dalmuri.domain.repository

import com.dalmuri.dalmuri.domain.model.Emotion
import com.dalmuri.dalmuri.domain.model.MonthlyReview
import com.dalmuri.dalmuri.domain.model.Til
import java.time.YearMonth

interface ReportRepository {
    suspend fun generateChartSummary(
        totalTilCount: Int,
        averageEmotionScore: Float,
        emotionCounts: Map<Emotion, Int>, // Emotion to count
    ): Result<String>

    suspend fun generateMonthlyReview(tils: List<Til>): Result<MonthlyReview>

    suspend fun saveChartSummary(
        yearMonth: String,
        chartSummary: String,
    ): Result<Unit>

    suspend fun getChartSummary(yearMonth: YearMonth): Result<String?>

    suspend fun saveMonthlyReview(
        yearMonth: YearMonth,
        review: MonthlyReview,
    ): Result<Unit>

    suspend fun getMonthlyReview(yearMonth: YearMonth): Result<MonthlyReview?>
}
