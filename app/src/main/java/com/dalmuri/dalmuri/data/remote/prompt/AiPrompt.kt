package com.dalmuri.dalmuri.data.remote.prompt

import com.dalmuri.dalmuri.domain.model.Emotion
import com.dalmuri.dalmuri.presentation.utils.emoji
import java.util.Locale

fun generateTilAnalysisPrompt(
    title: String,
    learned: String,
    difficulty: String?,
    tomorrow: String?,
): String =
    """
    당신은 개발자 학습 코치입니다. 아래 TIL(Today I Learned) 내용을 분석해주세요.

    [TIL 내용]
    제목: $title
    오늘 배운 것: $learned
    어려웠던 점: ${difficulty ?: "없음"}
    내일 할 일: ${tomorrow ?: "없음"}

    [분석 요청]
    다음 형식의 JSON으로만 응답해주세요:
    {
        "emotion": "성취감/만족/평범/어려움/좌절 중 하나",
        "emotionScore": 1-5 사이 정수,
        "difficultyLevel": "쉬움/보통/어려움/매우 어려움 중 하나",
        "comment": "격려나 조언 한 문장 (20자 이내)"
    }
    """.trimIndent()

fun generateChartSummaryPrompt(
    emotionCounts: Map<Emotion, Int>,
    totalTilCount: Int,
    averageEmotionScore: Float,
): String {
    val emotionStats =
        emotionCounts.entries.joinToString(", ") { "${it.key.emoji}(${it.value}회)" }

    return """
        당신은 학습 데이터 분석 전문가입니다. 사용자의 한 달간 TIL(Today I Learned) 데이터를 바탕으로 따뜻하고 격려하는 어조의 리포트를 작성해주세요.
        
        [데이터 요약]
        - 이번 달 총 학습 일수: ${totalTilCount}일
        - 평균 감정 점수: ${String.format(Locale.getDefault(), "%.1f", averageEmotionScore)}점
        - 감정별 횟수: $emotionStats
        
        [작성 가이드라인]
        1. 첫 문장은 학습 일수에 대한 칭찬으로 시작하세요.
        2. 평균 감정 점수와 적절한 이모지(예: 🤩, 😊, 😐)를 포함하여 전반적인 기분을 언급하세요.
        3. 데이터에서 보이는 특징적인 패턴(예: 특정 감정이 많을 때의 경향성 등)을 하나 짚어주세요. 
        4. 가독성을 위해 1~2문장 단위로 줄바꿈(\n\n)을 적절히 섞어서 작성하세요.
        5. 마지막은 다음 달을 위한 따뜻한 응원으로 마무리하세요.
        
        [응답 형식]
        반드시 아래 JSON 형식으로만 응답하세요:
        {
            "summary": "분석 결과 요약 내용"
        }
        """.trimIndent()
}

fun generateReviewPrompt(
    totalCount: Int,
    averageEmotionScore: Float,
    emotionDistribution: String,
    tilDetails: String,
): String =
    """
    당신은 성장을 돕는 학습 분석 전문가입니다. 사용자의 한 달간 TIL(Today I Learned) 데이터를 바탕으로 월간 회고 리포트를 작성해주세요.

    [한 달 요약 데이터]
    - 총 TIL 작성 수: ${totalCount}개
    - 평균 감정 점수: ${String.format(Locale.getDefault(), "%.1f", averageEmotionScore)}/5
    - 감정 분포: $emotionDistribution

    [TIL 상세 내역]
    $tilDetails

    [작성 요구사항]
    1. 월간 키워드: 학습 내용에서 가장 핵심적인 기술 스택이나 주제를 3~5개 추출하여 해시태그 형식으로 작성하세요. (예: #Kotlin, #Compose)
    2. Overall Mood: 한 달간의 전반적인 감정 상태를 요약하고 백분율로 표현하세요. (예: Excellent (75%), Good (60%) 등)
    3. Challenge Date: 한 달 중 가장 기술적으로 고군분투했거나 어려움을 겪었던 날의 날짜를 찾아 표기하세요. (예: 2026.02.14)
    4. 성장 포인트: 구체적으로 어떤 기술적 지식이나 문제 해결 능력이 향상되었는지 3~5개를 리스트로 작성하세요.
    5. 다음 달 조언: 이번 달의 학습 패턴과 남겨진 과제들을 바탕으로 다음 달에 시도해볼 만한 구체적인 조언을 작성하세요. (5~6줄) (가독성을 위해 줄바꿈(\n\n)을 적절히 섞어서 작성하세요.) 

    [응답 형식]
    반드시 아래 JSON 형식으로만 응답하세요:
    {
        "learningKeywords": ["#태그1", "#태그2"],
        "overallMood": "상태 (00%)",
        "challengeDate": "YYYY.MM.DD",
        "growthPoints": ["성장포인트1", "성장포인트2", "성장포인트3"],
        "nextMonthAdvice": "조언 내용"
    }
    """.trimIndent()
