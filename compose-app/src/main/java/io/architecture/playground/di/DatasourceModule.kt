package io.architecture.playground.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.architecture.database.imp.room.DefaultLocalDataSource
import io.architecture.datasource.api.LocalDataSource
import io.architecture.datasource.api.NetworkDataSource
import io.architecture.ktor.KtorNetworkDataSource
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