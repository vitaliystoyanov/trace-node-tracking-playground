package io.architecture.core.di

import org.koin.dsl.module

val coroutineModule = module {
    includes(coroutineDispatcherModule, coroutineScopeModule)
}