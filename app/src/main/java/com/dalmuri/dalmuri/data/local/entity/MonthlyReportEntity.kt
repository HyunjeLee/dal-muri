package com.dalmuri.dalmuri.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "monthly_reports")
data class MonthlyReportEntity(
    @PrimaryKey val yearMonth: String, // "2025-02" 형식의 식별자 (PK)
    // chart 화면용 AI 총평
    val chartSummary: String? = null,
    // 월간 회고용
    val learningKeywords: List<String>? = null, // 이번 달 주요 학습 키워드
    val overallMood: String? = null, // 전체적인 무드
    val challengeDate: String? = null, // 도전적인 날
    val growthPoints: List<String>? = null, // AI 분석 성장 포인트
    val nextMonthAdvice: String? = null, // 다음 달을 위한 조언
    val createdAt: Long, // 회고 생성 일시
)
