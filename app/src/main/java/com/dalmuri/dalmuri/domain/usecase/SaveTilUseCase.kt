package com.dalmuri.dalmuri.domain.usecase

import com.dalmuri.dalmuri.domain.model.Til
import com.dalmuri.dalmuri.domain.repository.TilRepository
import javax.inject.Inject

class SaveTilUseCase
    @Inject
    constructor(
        private val repository: TilRepository,
    ) {
        suspend operator fun invoke(
            title: String,
            learned: String,
            obstacles: String,
            tomorrow: String,
            aiAnalysis: Til.AiAnalysis? = null,
        ): Result<Long> {
            val til =
                Til(
                    title = title,
                    learned = learned,
                    obstacles = obstacles.ifBlank { null },
                    tomorrow = tomorrow.ifBlank { null },
                    createdAt = System.currentTimeMillis(),
                    emotion = aiAnalysis?.emotion,
                    emotionScore = aiAnalysis?.emotionScore,
                    difficultyLevel = aiAnalysis?.difficultyLevel,
                    aiComment = aiAnalysis?.comment,
                )

            return repository.saveTil(til)
        }
    }
