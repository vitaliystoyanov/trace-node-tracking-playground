package io.architecture.core.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Module(includes = [CoroutineDispatcherKoinModule::class])
internal class CoroutineScopeKoinModule {

    @Single
    @Named("applicationScope")
    fun providesCoroutineScope(
        @Named("defaultDispatcher") dispatcher: CoroutineDispatcher,
    ): CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher)

    @Single
    @Named("applicationIoScope")
    fun providesIoCoroutineScope(
        @Named("ioDispatcher") dispatcher: CoroutineDispatcher,
    ): CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher)
}



