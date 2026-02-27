package com.dalmuri.dalmuri.domain.usecase

import com.dalmuri.dalmuri.domain.model.Til
import com.dalmuri.dalmuri.domain.repository.TilRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject

class GetTilsByMonthUseCase
    @Inject
    constructor(
        private val repository: TilRepository,
    ) {
        operator fun invoke(
            year: Int,
            month: Int,
        ): Flow<Result<List<Til>>> {
            val (startTime, endTime) = getMonthRange(year, month)

            return repository
                .getTilsByMonth(startTime, endTime)
                .map { result ->
                    result.map { it }
                }
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
                        )
                        set(Calendar.HOUR_OF_DAY, 23)
                        set(Calendar.MINUTE, 59)
                        set(Calendar.SECOND, 59)
                        set(Calendar.MILLISECOND, 999)
                    }.timeInMillis

            return Pair(start, end)
        }
    }
