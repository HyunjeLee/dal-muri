package com.dalmuri.dalmuri.domain.model

enum class Emotion(
    val score: Int,
) {
    VERY_GOOD(5),
    GOOD(4),
    NORMAL(3),
    BAD(2),
    VERY_BAD(1),
    ;

    companion object {
        fun fromScore(score: Int?): Emotion = entries.find { it.score == score } ?: NORMAL
    }
}
