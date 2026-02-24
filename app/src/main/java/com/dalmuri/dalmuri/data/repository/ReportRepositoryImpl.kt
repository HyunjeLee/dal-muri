package com.dalmuri.dalmuri.data.repository

import com.dalmuri.dalmuri.data.remote.datasource.ReportRemoteDataSource
import com.dalmuri.dalmuri.domain.model.Emotion
import com.dalmuri.dalmuri.domain.repository.ReportRepository
import javax.inject.Inject

class ReportRepositoryImpl
    @Inject
    constructor(
        private val remoteDataSource: ReportRemoteDataSource,
    ) : ReportRepository {
        override suspend fun getChartSummary(
            totalTilCount: Int,
            averageEmotionScore: Float,
            emotionCounts: Map<Emotion, Int>,
        ): Result<String> =
            try {
                val chartSummary =
                    remoteDataSource.getChartSummary(totalTilCount, averageEmotionScore, emotionCounts)

                Result.success(chartSummary)
            } catch (e: Exception) {
                Result.failure(e)
            }

        override suspend fun saveMonthlyReport(
            yearMonth: String,
            chartSummary: String?,
        ): Result<Unit> {
            TODO("Not yet implemented")
        }
    }
