package com.dalmuri.dalmuri.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.dalmuri.dalmuri.data.local.entity.MonthlyReportEntity

@Dao
abstract class MonthlyReportDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract suspend fun insertIgnore(report: MonthlyReportEntity): Long

    @Query(
        """
        UPDATE monthly_reports 
        SET chartSummary = :summary 
        WHERE yearMonth = :yearMonth
    """,
    )
    protected abstract suspend fun updateChartSummary(
        yearMonth: String,
        summary: String,
    )

    @Query(
        """
        UPDATE monthly_reports 
        SET learningKeywords = :keywords, overallMood = :overallMood, challengeDate = :challengeDate, 
            growthPoints = :points, nextMonthAdvice = :advice 
        WHERE yearMonth = :yearMonth
    """,
    )
    protected abstract suspend fun updateReviewFields(
        yearMonth: String,
        keywords: List<String>,
        overallMood: String,
        challengeDate: String,
        points: List<String>,
        advice: String,
    )

    @Transaction
    open suspend fun upsertChartSummary(
        yearMonth: String,
        summary: String,
        createdAt: Long,
    ) {
        insertIgnore(MonthlyReportEntity(yearMonth = yearMonth, createdAt = createdAt))
        updateChartSummary(yearMonth, summary)
    }

    @Transaction
    open suspend fun upsertReviewFields(
        yearMonth: String,
        keywords: List<String>,
        overallMood: String,
        challengeDate: String,
        points: List<String>,
        advice: String,
        createdAt: Long,
    ) {
        insertIgnore(MonthlyReportEntity(yearMonth = yearMonth, createdAt = createdAt))
        updateReviewFields(yearMonth, keywords, overallMood, challengeDate, points, advice)
    }

    // 특정 월의 회고 가져오기
    @Query("SELECT * FROM monthly_reports WHERE yearMonth = :yearMonth")
    abstract suspend fun getReviewByMonth(yearMonth: String): MonthlyReportEntity?
}
