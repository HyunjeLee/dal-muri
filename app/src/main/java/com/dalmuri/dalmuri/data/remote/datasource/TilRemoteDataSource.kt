package com.dalmuri.dalmuri.data.remote.datasource

import com.dalmuri.dalmuri.data.remote.model.AiAnalysisDto

interface TilRemoteDataSource {
    suspend fun getTilAnalysis(
        title: String,
        learned: String,
        difficulty: String?,
        tomorrow: String?,
    ): AiAnalysisDto
}
