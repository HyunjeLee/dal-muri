package com.dalmuri.dalmuri.data.repository

import com.dalmuri.dalmuri.data.local.datasource.ReportLocalDataSource
import com.dalmuri.dalmuri.data.remote.datasource.ReportRemoteDataSource
import com.dalmuri.dalmuri.domain.model.Emotion
import com.dalmuri.dalmuri.domain.repository.ReportRepository
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
    }
