package com.dalmuri.dalmuri.presentation.chart.dummy

import com.dalmuri.dalmuri.domain.model.Emotion
import com.dalmuri.dalmuri.domain.model.MonthlyChartData
import java.time.YearMonth

val dummyStats =
    MonthlyChartData(
        yearMonth = YearMonth.of(2026, 2),
        emotionCounts =
            mapOf(
                Emotion.VERY_GOOD to 10,
                Emotion.GOOD to 5,
                Emotion.NORMAL to 3,
                Emotion.BAD to 2,
                Emotion.VERY_BAD to 2,
            ),
        dailyEmotionScores =
            listOf(
                MonthlyChartData.DailyEmotion(1738368000000L, Emotion.NORMAL),
                MonthlyChartData.DailyEmotion(1738454400000L, Emotion.GOOD),
                MonthlyChartData.DailyEmotion(1738540800000L, Emotion.VERY_GOOD),
                MonthlyChartData.DailyEmotion(1738627200000L, Emotion.GOOD),
                MonthlyChartData.DailyEmotion(1738713600000L, Emotion.NORMAL),
                MonthlyChartData.DailyEmotion(1738800000000L, Emotion.BAD),
            ),
        totalTilCount = 22,
        averageEmotionScore = 4.2f,
    )
