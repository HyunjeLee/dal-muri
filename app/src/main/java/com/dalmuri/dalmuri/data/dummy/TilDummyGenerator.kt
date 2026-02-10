package com.dalmuri.dalmuri.data.dummy

import com.dalmuri.dalmuri.domain.model.Til
import java.util.Calendar

object TilDummyGenerator {
    fun generateDummyTils(): List<Til> {
        val tils = mutableListOf<Til>()
        val calendar = Calendar.getInstance()

        // Today
        tils.add(createDummyTil(calendar.timeInMillis, "Compose 상태 관리 학습", 4))
        tils.add(createDummyTil(calendar.timeInMillis - 3600000, "Room DB 기초 정리", 3))

        // Yesterday
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        tils.add(createDummyTil(calendar.timeInMillis, "Coroutine 삽질", 2))
        tils.add(createDummyTil(calendar.timeInMillis - 7200000, "Andorid UI 테스트", 5))

        // 2 days ago
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        tils.add(createDummyTil(calendar.timeInMillis, "Dependency Injection (Hilt)", 1))

        // 3 days ago
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        tils.add(createDummyTil(calendar.timeInMillis, "Clean Architecture 개요", 5))

        return tils
    }

    private fun createDummyTil(
        timestamp: Long,
        title: String,
        fixedScore: Int? = null,
    ): Til {
        // 감정 점수는 1~5 랜덤, 또는 고정값 사용
        val score = fixedScore ?: (1..5).random()

        return Til(
            id = System.currentTimeMillis() + (0..10000).random(),
            title = title,
            learned = "Dummy Content for $title",
            createdAt = timestamp,
            emotionScore = score,
        )
    }
}
