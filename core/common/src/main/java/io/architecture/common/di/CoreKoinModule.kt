package io.architecture.common.di

import org.koin.core.annotation.Module

@Module(includes = [CoroutineScopeKoinModule::class])
class CoreKoinModule