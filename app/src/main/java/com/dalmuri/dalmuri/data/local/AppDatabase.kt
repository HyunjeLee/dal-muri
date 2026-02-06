package com.dalmuri.dalmuri.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dalmuri.dalmuri.data.local.dao.MonthlyReportDao
import com.dalmuri.dalmuri.data.local.dao.TilDao
import com.dalmuri.dalmuri.data.local.entity.MonthlyReportEntity
import com.dalmuri.dalmuri.data.local.entity.TilEntity

@Database(
    entities = [TilEntity::class, MonthlyReportEntity::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tilDao(): TilDao

    abstract fun monthlyReportDao(): MonthlyReportDao
}
