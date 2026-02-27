package com.dalmuri.dalmuri.data.repository

import com.dalmuri.dalmuri.data.local.datasource.ReportLocalDataSource
import com.dalmuri.dalmuri.data.remote.datasource.ReportRemoteDataSource
import com.dalmuri.dalmuri.domain.model.Emotion
import com.dalmuri.dalmuri.domain.model.MonthlyReview
import com.dalmuri.dalmuri.domain.model.Til
import com.dalmuri.dalmuri.domain.repository.ReportRepository
import com.dalmuri.dalmuri.presentation.utils.emoji
import com.dalmuri.dalmuri.presentation.utils.toFormattedDate
import java.time.YearMonth
import javax.inject.Inject

class ReportRepositoryImpl
    @Inject
    constructor(
        private val localDataSource: ReportLocalDataSource,
        private val remoteDataSource: ReportRemoteDataSource,
    ) : ReportRepository {
        override suspend fun generateChartSummary(
            totalTilCount: Int,
            averageEmotionScore: Float,
            emotionCounts: Map<Emotion, Int>,
        ): Result<String> =
            try {
                val chartSummary =
                    remoteDataSource.generateChartSummary(totalTilCount, averageEmotionScore, emotionCounts)

                Result.success(chartSummary)
            } catch (e: Exception) {
                Result.failure(e)
            }

        override suspend fun generateMonthlyReview(tils: List<Til>): Result<MonthlyReview> =
            try {
                val totalCount = tils.size
                val averageEmotionScore =
                    if (tils.isNotEmpty()) {
                        tils.mapNotNull { it.emotionScore?.score }.sum().toFloat() / totalCount
                    } else {
                        0f
                    }

                val emotionDistribution =
                    tils
                        .mapNotNull { it.emotionScore }
                        .groupBy { it }
                        .mapValues { it.value.size }
                        .entries
                        .joinToString(", ") { "${it.key.emoji}(${it.value}회)" }

                val tilDetails =
                    tils.sortedBy { it.createdAt }.joinToString("\n\n") { til ->
                        """
                        날짜: ${til.createdAt.toFormattedDate()}
                        제목: ${til.title}
                        오늘 배운 것: ${til.learned}
                        어려웠던 점: ${til.obstacles ?: "없음"}
                        내일의 목표: ${til.tomorrow ?: "없음"}
                        """.trimIndent()
                    }

                val dto =
                    remoteDataSource.generateMonthlyReview(
                        totalCount = totalCount,
                        averageEmotionScore = averageEmotionScore,
                        emotionDistribution = emotionDistribution,
                        tilDetails = tilDetails,
                    )

                Result.success(dto.toDomain())
            } catch (e: Exception) {
                Result.failure(e)
            }

        override suspend fun saveChartSummary(
            yearMonth: String,
            chartSummary: String,
        ): Result<Unit> =
            try {
                localDataSource.insertChartSummary(
                    yearMonth = yearMonth,
                    summary = chartSummary,
                    createdAt = System.currentTimeMillis(),
                )

                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }

        override suspend fun getChartSummary(yearMonth: YearMonth): Result<String?> =
            try {
                val summary = localDataSource.getChartSummary(yearMonth.toString())

                Result.success(summary)
            } catch (e: Exception) {
                Result.failure(e)
            }
    }
