package com.dalmuri.dalmuri.domain.usecase

import com.dalmuri.dalmuri.domain.model.MonthlyReview
import com.dalmuri.dalmuri.domain.repository.ReportRepository
import java.time.YearMonth
import javax.inject.Inject

class SaveMonthlyReviewUseCase
    @Inject
    constructor(
        private val repository: ReportRepository,
    ) {
        suspend operator fun invoke(
            yearMonth: YearMonth,
            review: MonthlyReview,
        ): Result<Unit> = repository.saveMonthlyReview(yearMonth, review)
    }
