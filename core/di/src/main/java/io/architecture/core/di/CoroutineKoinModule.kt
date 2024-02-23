package io.architecture.core.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(includes = [CoroutineScopeKoinModule::class, CoroutineDispatcherKoinModule::class])
@ComponentScan("io.architecture.common")
class CoroutineKoinModule