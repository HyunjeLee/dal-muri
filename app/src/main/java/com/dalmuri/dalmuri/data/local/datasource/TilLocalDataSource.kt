package com.dalmuri.dalmuri.data.local.datasource

import com.dalmuri.dalmuri.data.local.entity.TilEntity

interface TilLocalDataSource {
    suspend fun insertTil(til: TilEntity): Long

    suspend fun getTilById(id: Long): TilEntity?
}
