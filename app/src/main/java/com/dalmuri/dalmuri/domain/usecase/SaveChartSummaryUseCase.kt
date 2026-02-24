package com.dalmuri.dalmuri.domain.usecase

import com.dalmuri.dalmuri.domain.repository.ReportRepository
import javax.inject.Inject

class SaveChartSummaryUseCase
    @Inject
    constructor(
        private val repository: ReportRepository,
    ) {
        suspend operator fun invoke(
            yearMonth: String,
            summary: String,
        ): Result<Unit> =
            repository.saveChartSummary(
                yearMonth = yearMonth,
                chartSummary = summary,
            )
    }
