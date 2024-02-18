package io.architecture.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.architecture.data.repository.DefaultConnectionStateRepository
import io.architecture.data.repository.DefaultRouteRepository
import io.architecture.data.repository.DefaultTraceRepository
import io.architecture.data.repository.interfaces.ConnectionStateRepository
import io.architecture.data.repository.interfaces.DefaultNodeRepository
import io.architecture.data.repository.interfaces.NodeRepository
import io.architecture.data.repository.interfaces.RouteRepository
import io.architecture.data.repository.interfaces.TraceRepository
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

    @Binds
    @Singleton
    abstract fun bindsConnectionStateRepository(repository: DefaultConnectionStateRepository): ConnectionStateRepository

}