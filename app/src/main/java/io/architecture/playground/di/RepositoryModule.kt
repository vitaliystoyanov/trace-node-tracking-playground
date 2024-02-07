package io.architecture.playground.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.architecture.playground.data.repository.DefaultNodeRepository
import io.architecture.playground.data.repository.DefaultRouteRepository
import io.architecture.playground.data.repository.interfaces.NodeRepository
import io.architecture.playground.data.repository.interfaces.RouteRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsRouteRepository(repository: DefaultNodeRepository): NodeRepository

    @Binds
    @Singleton
    abstract fun bindsNodeRepository(repository: DefaultRouteRepository): RouteRepository

}