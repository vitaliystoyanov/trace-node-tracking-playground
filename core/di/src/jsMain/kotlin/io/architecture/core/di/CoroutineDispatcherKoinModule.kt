package io.architecture.core.di

import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual val coroutineDispatcherModule = module {
    single(named("ioDispatcher")) { Dispatchers.Default } // Replaced
    single(named("defaultDispatcher")) { Dispatchers.Default }
    single(named("mainDispatcher")) { Dispatchers.Main }
}