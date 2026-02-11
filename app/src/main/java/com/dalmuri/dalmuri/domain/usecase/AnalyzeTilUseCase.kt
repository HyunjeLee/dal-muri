package com.dalmuri.dalmuri.domain.usecase

import com.dalmuri.dalmuri.domain.model.Til
import com.dalmuri.dalmuri.domain.repository.TilRepository
import javax.inject.Inject

class AnalyzeTilUseCase
    @Inject
    constructor(
        private val repository: TilRepository,
    ) {
        suspend operator fun invoke(
            title: String,
            learned: String,
            difficulty: String? = null,
            tomorrow: String? = null,
        ): Result<Til.AiAnalysis> = repository.analyzeTil(title, learned, difficulty, tomorrow)
    }
