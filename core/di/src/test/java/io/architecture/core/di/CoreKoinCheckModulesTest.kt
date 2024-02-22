package io.architecture.core.di

import io.architecture.core.runtime.configuration.RuntimeConfigurationModule
import io.architecture.data.repositoryModule
import io.architecture.database.imp.room.di.roomDaoModule
import io.architecture.database.imp.room.di.roomDatabaseModule
import io.architecture.domain.di.useCaseModule
import io.architecture.network.websocket.imp.ktor.di.ktorClientModule
import io.architecture.network.websocket.imp.ktor.di.ktorModule
import io.architecture.network.websocket.imp.ktor.di.ktorServiceModule
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.ksp.generated.module
import org.koin.test.verify.verify

class CoreKoinCheckModulesTest {

    private val coreModule = module {
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

    @Test
    @OptIn(KoinExperimentalAPI::class)
    fun checkKoinModule() {
        coreModule.verify(
            extraTypes = listOf(
                kotlin.reflect.KClass::class,
                io.ktor.client.engine.HttpClientEngine::class,
                io.ktor.client.HttpClientConfig::class
            )
        )
    }
}