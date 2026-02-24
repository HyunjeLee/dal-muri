package com.dalmuri.dalmuri.data.local.datasource

interface ReportLocalDataSource {
    suspend fun insertChartSummary(
        yearMonth: String,
        summary: String,
        createdAt: Long,
    )

    suspend fun getChartSummary(yearMonth: String): String?
}
