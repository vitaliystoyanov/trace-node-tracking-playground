package io.architecture.core.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val coroutineScopeModule = module {
    single(named("applicationScope")) {
        CoroutineScope(
            SupervisorJob() + get<CoroutineDispatcher>(
                named("defaultDispatcher")
            )
        )
    }
    single(named("applicationIoScope")) {
        CoroutineScope(
            SupervisorJob() + get<CoroutineDispatcher>(
                named("ioDispatcher")
            )
        )
    }
}



