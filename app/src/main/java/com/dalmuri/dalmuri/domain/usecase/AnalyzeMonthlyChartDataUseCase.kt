package com.dalmuri.dalmuri.domain.usecase

import com.dalmuri.dalmuri.domain.model.Emotion
import com.dalmuri.dalmuri.domain.repository.ReportRepository
import javax.inject.Inject

class AnalyzeMonthlyChartDataUseCase
    @Inject
    constructor(
        private val repository: ReportRepository,
    ) {
        suspend operator fun invoke(
            totalTilCount: Int,
            averageEmotionScore: Float,
            emotionCounts: Map<Emotion, Int>,
        ): Result<String> = repository.generateChartSummary(totalTilCount, averageEmotionScore, emotionCounts)
    }
