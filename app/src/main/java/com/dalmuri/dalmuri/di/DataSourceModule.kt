package com.dalmuri.dalmuri.di

import com.dalmuri.dalmuri.data.local.datasource.TilLocalDataSource
import com.dalmuri.dalmuri.data.local.datasource.TilLocalDataSourceImpl
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
}
