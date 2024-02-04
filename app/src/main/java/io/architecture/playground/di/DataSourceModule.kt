package io.architecture.playground.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.architecture.playground.data.local.DefaultLocalNodesDataSource
import io.architecture.playground.data.local.LocalNodesDataSource
import io.architecture.playground.data.remote.DefaultNetworkNodeDataSource
import io.architecture.playground.data.remote.NetworkNodeDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Singleton
    @Binds
    abstract fun bindNetworkDataSource(dataSource: DefaultNetworkNodeDataSource): NetworkNodeDataSource


    @Singleton
    @Binds
    abstract fun bindLocalDataSource(dataSource: DefaultLocalNodesDataSource): LocalNodesDataSource
}