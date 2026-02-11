package com.dalmuri.dalmuri.data.local.datasource

import com.dalmuri.dalmuri.data.local.dao.TilDao
import com.dalmuri.dalmuri.data.local.entity.TilEntity
import javax.inject.Inject

class TilLocalDataSourceImpl
    @Inject
    constructor(
        private val tilDao: TilDao,
    ) : TilLocalDataSource {
        override suspend fun insertTil(til: TilEntity): Long = tilDao.insertTil(til)

        override suspend fun getTilById(id: Long): TilEntity? = tilDao.getTilById(id)
    }
