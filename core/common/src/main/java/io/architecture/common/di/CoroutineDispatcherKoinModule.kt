package io.architecture.common.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Module
internal class CoroutineDispatcherKoinModule {

    @Single
    @Named("ioDispatcher")
    fun providesIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Single
    @Named("defaultDispatcher")
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
}