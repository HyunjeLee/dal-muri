package com.dalmuri.dalmuri.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "monthly_reports")
data class MonthlyReportEntity(
    @PrimaryKey val yearMonth: String, // "2025-02" 형식의 식별자 (PK)
    // summary 화면용 AI 총평
    val summaryComment: String?,
    // 월간 회고용
    val learningSummary: List<String>, // 이번 달 주요 학습 키워드
    val growthPoints: List<String>, // AI 분석 성장 포인트
    val nextMonthAdvice: String, // 다음 달을 위한 조언
    val createdAt: Long, // 회고 생성 일시
)
