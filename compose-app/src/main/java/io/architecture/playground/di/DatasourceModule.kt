package io.architecture.playground.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.architecture.datasource.api.NetworkDataSource
import io.architecture.network.websocket.imp.ktor.KtorNetworkDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DatasourceModule {

    @Singleton
    @Binds
    abstract fun bindNetworkDataSource(dataSource: KtorNetworkDataSource): NetworkDataSource

}