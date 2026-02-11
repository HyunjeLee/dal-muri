package com.dalmuri.dalmuri.data.repository

import com.dalmuri.dalmuri.data.local.dao.TilDao
import com.dalmuri.dalmuri.data.mapper.toEntity
import com.dalmuri.dalmuri.data.remote.datasource.TilRemoteDataSource
import com.dalmuri.dalmuri.domain.model.Til
import com.dalmuri.dalmuri.domain.repository.TilRepository
import javax.inject.Inject

class TilRepositoryImpl
    @Inject
    constructor(
        private val remoteDataSource: TilRemoteDataSource,
        private val tilDao: TilDao,
    ) : TilRepository {
        override suspend fun analyzeTil(
            title: String,
            learned: String,
            difficulty: String?,
            tomorrow: String?,
        ): Result<Til.AiAnalysis> =
            try {
                val dto = remoteDataSource.getTilAnalysis(title, learned, difficulty, tomorrow)
                Result.success(dto.toDomain())
            } catch (e: Exception) {
                Result.failure(e)
            }

        override suspend fun saveTil(til: Til): Result<Long> =
            try {
                val id = tilDao.insertTil(til.toEntity())
                Result.success(id)
            } catch (e: Exception) {
                Result.failure(e)
            }
    }
