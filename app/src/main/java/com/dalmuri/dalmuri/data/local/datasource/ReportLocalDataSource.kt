package com.dalmuri.dalmuri.data.local.datasource

import com.dalmuri.dalmuri.data.local.entity.MonthlyReportEntity

interface ReportLocalDataSource {
    suspend fun insertChartSummary(
        yearMonth: String,
        summary: String,
        createdAt: Long,
    )

    suspend fun getChartSummary(yearMonth: String): String?

    suspend fun insertMonthlyReview(
        yearMonth: String,
        keywords: List<String>,
        overallMood: String,
        challengeDate: String,
        points: List<String>,
        advice: String,
        createdAt: Long,
    )

    suspend fun getMonthlyReview(yearMonth: String): MonthlyReportEntity?
}
