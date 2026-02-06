package com.dalmuri.dalmuri.di

import android.content.Context
import androidx.room.Room
import com.dalmuri.dalmuri.data.local.AppDatabase
import com.dalmuri.dalmuri.data.local.dao.MonthlyReportDao
import com.dalmuri.dalmuri.data.local.dao.TilDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase =
        Room
            .databaseBuilder(
                context,
                AppDatabase::class.java,
                "til_database",
            ).build()

    @Provides fun provideTilDao(database: AppDatabase): TilDao = database.tilDao()

    @Provides
    fun provideMonthlyReportDao(database: AppDatabase): MonthlyReportDao = database.monthlyReportDao()
}
