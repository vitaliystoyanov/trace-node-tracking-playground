package io.architecture.playground.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.architecture.playground.data.local.DefaultLocalNodesDataSource
import io.architecture.playground.data.local.LocalNodeRouteDataSource
import io.architecture.playground.data.remote.DefaultNetworkDataSource
import io.architecture.playground.data.remote.interfaces.NetworkDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Singleton
    @Binds
    abstract fun bindNetworkDataSource(dataSource: DefaultNetworkDataSource): NetworkDataSource


    @Singleton
    @Binds
    abstract fun bindLocalDataSource(dataSource: DefaultLocalNodesDataSource): LocalNodeRouteDataSource
}