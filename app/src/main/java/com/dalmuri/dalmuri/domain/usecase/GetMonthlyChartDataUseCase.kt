package com.dalmuri.dalmuri.domain.usecase

import com.dalmuri.dalmuri.domain.model.Emotion
import com.dalmuri.dalmuri.domain.model.MonthlyChartData
import com.dalmuri.dalmuri.domain.repository.TilRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.time.YearMonth
import java.util.Calendar
import javax.inject.Inject

class GetMonthlyChartDataUseCase
    @Inject
    constructor(
        private val repository: TilRepository,
    ) {
        operator fun invoke(
            year: Int,
            month: Int,
        ): Flow<Result<MonthlyChartData>> {
            val (startTime, endTime) = getMonthRange(year, month)

            return repository
                .getTilsByMonth(startTime, endTime)
                .map { tils ->
                    val emotionCounts =
                        tils.mapNotNull { it.emotionScore }.groupBy { it }.mapValues {
                            it.value.size
                        }

                    val dailyEmotionScores =
                        tils.sortedBy { it.createdAt }.map {
                            MonthlyChartData.DailyEmotion(
                                timestamp = it.createdAt,
                                emotion = it.emotionScore ?: Emotion.NORMAL,
                            )
                        }

                    val averageScore =
                        if (tils.isNotEmpty()) {
                            tils.sumOf { it.emotionScore?.score ?: 0 }.toFloat() / tils.size
                        } else {
                            0f
                        }

                    Result.success(
                        MonthlyChartData(
                            yearMonth = YearMonth.of(year, month),
                            emotionCounts = emotionCounts,
                            dailyEmotionScores = dailyEmotionScores,
                            totalTilCount = tils.size,
                            averageEmotionScore = averageScore,
                        ),
                    )
                }.catch { e -> emit(Result.failure(e)) }
        }

        private fun getMonthRange(
            year: Int,
            month: Int,
        ): Pair<Long, Long> {
            val start =
                Calendar
                    .getInstance()
                    .apply {
                        set(year, month - 1, 1, 0, 0, 0)
                        set(Calendar.MILLISECOND, 0)
                    }.timeInMillis

            val end =
                Calendar
                    .getInstance()
                    .apply {
                        set(year, month - 1, 1, 23, 59, 59)
                        set(
                            Calendar.DAY_OF_MONTH,
                            getActualMaximum(Calendar.DAY_OF_MONTH),
                        ) // 실제 해당 월의 마지막 일자
                        set(Calendar.MILLISECOND, 999)
                    }.timeInMillis

            return Pair(start, end)
        }
    }
