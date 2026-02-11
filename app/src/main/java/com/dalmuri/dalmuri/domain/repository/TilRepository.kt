package com.dalmuri.dalmuri.domain.repository

import com.dalmuri.dalmuri.domain.model.Til

interface TilRepository {
    suspend fun analyzeTil(
        title: String,
        learned: String,
        difficulty: String?,
        tomorrow: String?,
    ): Result<Til.AiAnalysis>

    suspend fun saveTil(til: Til): Result<Long>

    suspend fun getTil(id: Long): Result<Til>
}
