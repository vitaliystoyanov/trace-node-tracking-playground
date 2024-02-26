package io.architecture.core.di

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO // TODO Different import that import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

val coroutineDispatcherModule = module {
    single(named("ioDispatcher")) { Dispatchers.IO }
    single(named("defaultDispatcher")) { Dispatchers.Default }
    single(named("mainDispatcher")) { Dispatchers.Main }
}