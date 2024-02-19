package io.architecture.playground.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.architecture.playground.data.local.DefaultLocalDataSource
import io.architecture.playground.data.local.LocalDataSource
import io.architecture.playground.data.remote.KtorNetworkDataSource
import io.architecture.playground.data.remote.NetworkDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DatasourceModule {

    @Singleton
    @Binds
    abstract fun bindNetworkDataSource(dataSource: KtorNetworkDataSource): NetworkDataSource

    @Singleton
    @Binds
    abstract fun bindLocalDataSource(dataSource: DefaultLocalDataSource): LocalDataSource
}