package com.dalmuri.dalmuri.data.local.datasource

import com.dalmuri.dalmuri.data.local.entity.TilEntity
import kotlinx.coroutines.flow.Flow

interface TilLocalDataSource {
    suspend fun insertTil(til: TilEntity): Long

    suspend fun getTilById(id: Long): TilEntity?

    fun getAllTils(): Flow<List<TilEntity>>

    suspend fun deleteTil(id: Long)
}
