package com.dalmuri.dalmuri.data.remote.datasource

import com.dalmuri.dalmuri.domain.model.Emotion

interface ReportRemoteDataSource {
    suspend fun getChartSummary(
        totalTilCount: Int,
        averageEmotionScore: Float,
        emotionCounts: Map<Emotion, Int>,
    ): String
}
