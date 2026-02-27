package com.dalmuri.dalmuri.domain.usecase

import com.dalmuri.dalmuri.domain.model.MonthlyReview
import com.dalmuri.dalmuri.domain.model.Til
import com.dalmuri.dalmuri.domain.repository.ReportRepository
import javax.inject.Inject

class AnalyzeMonthlyReviewUseCase
    @Inject
    constructor(
        private val repository: ReportRepository,
    ) {
        suspend operator fun invoke(tils: List<Til>): Result<MonthlyReview> = repository.generateMonthlyReview(tils)
    }
