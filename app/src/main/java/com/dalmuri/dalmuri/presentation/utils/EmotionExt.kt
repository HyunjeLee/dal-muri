package com.dalmuri.dalmuri.presentation.utils

import androidx.compose.ui.graphics.Color
import com.dalmuri.dalmuri.domain.model.Emotion

/** 감정(Emotion) Enum에 대한 UI 표현 확장 프로퍼티 */
val Emotion?.color: Color
    get() =
        when (this) {
            Emotion.VERY_GOOD -> Color(0xFF8B8000) // Deep Yellow
            Emotion.GOOD -> Color(0xFFFFD700) // Gold
            Emotion.NORMAL -> Color(0xFF90EE90) // Light Green
            Emotion.BAD -> Color(0xFFADD8E6) // Light Blue
            Emotion.VERY_BAD -> Color(0xFFFFB6C1) // Light Pink
            null -> Color.Gray
        }

val Emotion?.emoji: String
    get() =
        when (this) {
            Emotion.VERY_GOOD -> "🎉"
            Emotion.GOOD -> "😊"
            Emotion.NORMAL -> "😐"
            Emotion.BAD -> "😓"
            Emotion.VERY_BAD -> "😢"
            null -> "😐"
        }
