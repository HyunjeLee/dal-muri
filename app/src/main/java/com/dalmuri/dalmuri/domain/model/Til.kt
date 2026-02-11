package com.dalmuri.dalmuri.domain.model

data class Til(
    val id: Long = 0,
    val title: String, // TIL 제목
    val learned: String, // 오늘 배운 것
    val obstacles: String? = null, // 어려웠던 점 (선택)
    val tomorrow: String? = null, // 내일 할 일 (선택)
    val createdAt: Long, // 생성 일시 (timestamp)
    val updatedAt: Long? = createdAt, // 수정 일시 (timestamp)
    // AI 분석 결과 // 상세 화면 내 첫 카드
    val emotion: String? = null, // 감정 (AI 분석 결과)
    val emotionScore: Int? = null, // 감정 점수 (1-5)
    val difficultyLevel: String? = null, // 난이도 (쉬움, 보통 등)
    val aiComment: String? = null, // AI의 한줄 응원/조언
) {
    val emoji: String
        get() =
            when (emotionScore) {
                5 -> "🎉"
                4 -> "😊"
                3 -> "😐"
                2 -> "😓"
                else -> "😢"
            }

    data class AiAnalysis(
        val emotion: String,
        val emotionScore: Int,
        val difficultyLevel: String,
        val comment: String,
    )
}
