package io.architecture.playground.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.architecture.playground.data.remote.DefaultNetworkDiverTraceDataSource
import io.architecture.playground.data.remote.NetworkDiverTraceDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Singleton
    @Binds
    abstract fun bindNetworkDataSource(dataSource: DefaultNetworkDiverTraceDataSource): NetworkDiverTraceDataSource
}