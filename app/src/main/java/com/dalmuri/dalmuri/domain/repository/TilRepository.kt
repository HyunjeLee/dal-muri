package com.dalmuri.dalmuri.domain.repository

import com.dalmuri.dalmuri.domain.model.Til
import kotlinx.coroutines.flow.Flow

interface TilRepository {
    suspend fun analyzeTil(
        title: String,
        learned: String,
        difficulty: String?,
        tomorrow: String?,
    ): Result<Til.AiAnalysis>

    suspend fun saveTil(til: Til): Result<Long>

    suspend fun getTil(id: Long): Result<Til>

    fun getAllTils(): Flow<List<Til>>
}
