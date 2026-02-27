package com.dalmuri.dalmuri.data.local.datasource

import com.dalmuri.dalmuri.data.local.dao.MonthlyReportDao
import com.dalmuri.dalmuri.data.local.entity.MonthlyReportEntity
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

        override suspend fun insertMonthlyReview(
            yearMonth: String,
            keywords: List<String>,
            overallMood: String,
            challengeDate: String,
            points: List<String>,
            advice: String,
            createdAt: Long,
        ) {
            dao.upsertReviewFields(
                yearMonth,
                keywords,
                overallMood,
                challengeDate,
                points,
                advice,
                createdAt,
            )
        }

        override suspend fun getMonthlyReview(yearMonth: String): MonthlyReportEntity? = dao.getReviewByMonth(yearMonth)
    }
