package io.architecture.playground.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.architecture.playground.data.DefaultDiverTraceRepository
import io.architecture.playground.data.DiverTraceRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindDiverTraceRepository(repository: DefaultDiverTraceRepository): DiverTraceRepository
}

