package com.dalmuri.dalmuri.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dalmuri.dalmuri.data.local.entity.MonthlyReportEntity

@Dao
interface MonthlyReportDao {
    // 월간 회고 저장 (이미 있으면 덮어쓰기)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMonthlyReview(review: MonthlyReportEntity)

    // 특정 월의 회고 가져오기
    @Query("SELECT * FROM monthly_reports WHERE yearMonth = :yearMonth")
    suspend fun getReviewByMonth(yearMonth: String): MonthlyReportEntity?
}
