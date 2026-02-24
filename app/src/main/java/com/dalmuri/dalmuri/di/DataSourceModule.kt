package com.dalmuri.dalmuri.di

import com.dalmuri.dalmuri.data.local.datasource.TilLocalDataSource
import com.dalmuri.dalmuri.data.local.datasource.TilLocalDataSourceImpl
import com.dalmuri.dalmuri.data.remote.datasource.ReportRemoteDataSource
import com.dalmuri.dalmuri.data.remote.datasource.ReportRemoteDataSourceImpl
import com.dalmuri.dalmuri.data.remote.datasource.TilRemoteDataSource
import com.dalmuri.dalmuri.data.remote.datasource.TilRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindTilLocalDataSource(tilLocalDataSourceImpl: TilLocalDataSourceImpl): TilLocalDataSource

    @Binds
    @Singleton
    abstract fun bindTilRemoteDataSource(tilRemoteDataSourceImpl: TilRemoteDataSourceImpl): TilRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindReportRemoteDataSource(reportRemoteDataSourceImpl: ReportRemoteDataSourceImpl): ReportRemoteDataSource
}
