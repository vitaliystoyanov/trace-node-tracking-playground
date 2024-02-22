package io.architecture.core.di

import io.architecture.core.runtime.configuration.RuntimeConfigurationModule
import io.architecture.data.repositoryModule
import io.architecture.database.imp.room.di.roomDaoModule
import io.architecture.database.imp.room.di.roomDatabaseModule
import io.architecture.domain.di.useCaseModule
import io.architecture.network.websocket.imp.ktor.di.ktorClientModule
import io.architecture.network.websocket.imp.ktor.di.ktorModule
import io.architecture.network.websocket.imp.ktor.di.ktorServiceModule
import org.koin.dsl.module
import org.koin.ksp.generated.module

val coreKoinModules = module {
    includes(
        CoroutineKoinModule().module,
        RuntimeConfigurationModule().module,
        repositoryModule,
        useCaseModule,
        roomDaoModule,
        roomDatabaseModule,
        ktorModule,
        ktorClientModule,
        ktorServiceModule,
    )
}