package io.architecture.playground.data.repository.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.architecture.playground.data.repository.DefaultNodeRepository
import io.architecture.playground.data.repository.DefaultRouteRepository
import io.architecture.playground.data.repository.DefaultTraceRepository
import io.architecture.playground.data.repository.interfaces.NodeRepository
import io.architecture.playground.data.repository.interfaces.RouteRepository
import io.architecture.playground.data.repository.interfaces.TraceRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsNodeRepository(repository: DefaultNodeRepository): NodeRepository

    @Binds
    @Singleton
    abstract fun bindsTraceRepository(repository: DefaultTraceRepository): TraceRepository

    @Binds
    @Singleton
    abstract fun bindsRouteRepository(repository: DefaultRouteRepository): RouteRepository

}