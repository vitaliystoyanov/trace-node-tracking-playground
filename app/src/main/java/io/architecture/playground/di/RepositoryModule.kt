package io.architecture.playground.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.architecture.playground.data.DefaultTraceRepository
import io.architecture.playground.data.TraceRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindDiverTraceRepository(repository: DefaultTraceRepository): TraceRepository
}

