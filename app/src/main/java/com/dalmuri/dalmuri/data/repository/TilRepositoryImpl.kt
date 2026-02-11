package com.dalmuri.dalmuri.data.repository

import com.dalmuri.dalmuri.data.local.datasource.TilLocalDataSource
import com.dalmuri.dalmuri.data.mapper.toDomain
import com.dalmuri.dalmuri.data.mapper.toEntity
import com.dalmuri.dalmuri.data.remote.datasource.TilRemoteDataSource
import com.dalmuri.dalmuri.domain.model.Til
import com.dalmuri.dalmuri.domain.repository.TilRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TilRepositoryImpl
    @Inject
    constructor(
        private val remoteDataSource: TilRemoteDataSource,
        private val localDataSource: TilLocalDataSource,
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
                val id = localDataSource.insertTil(til.toEntity())
                Result.success(id)
            } catch (e: Exception) {
                Result.failure(e)
            }

        override suspend fun getTil(id: Long): Result<Til> =
            try {
                val entity = localDataSource.getTilById(id)
                if (entity != null) {
                    Result.success(entity.toDomain())
                } else {
                    Result.failure(NoSuchElementException("TIL not found with id: $id"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }

        override fun getAllTils(): Flow<List<Til>> = localDataSource.getAllTils().map { entities -> entities.map { it.toDomain() } }

        override suspend fun deleteTil(id: Long): Result<Unit> =
            try {
                localDataSource.deleteTil(id)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
    }
