package com.dalmuri.dalmuri.data.local.datasource

import com.dalmuri.dalmuri.data.local.dao.MonthlyReportDao
import javax.inject.Inject

class ReportLocalDataSourceImpl
    @Inject
    constructor(
        private val dao: MonthlyReportDao,
    ) : ReportLocalDataSource {
        override suspend fun insertChartSummary(
            yearMonth: String,
            summary: String,
            createdAt: Long,
        ) {
            dao.upsertChartSummary(yearMonth, summary, createdAt)
        }

        override suspend fun getChartSummary(yearMonth: String): String? = dao.getReviewByMonth(yearMonth)?.chartSummary
    }
