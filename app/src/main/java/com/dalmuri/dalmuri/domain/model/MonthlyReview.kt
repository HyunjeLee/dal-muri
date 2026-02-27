package com.dalmuri.dalmuri.domain.model

data class MonthlyReview(
    val keywords: List<String> = emptyList(),
    val overallMood: String = "",
    val challengeDate: String = "",
    val growthPoints: List<String> = emptyList(),
    val nextMonthAdvice: String = "",
)
