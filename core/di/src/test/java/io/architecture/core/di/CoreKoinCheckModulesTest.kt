package io.architecture.core.di

import io.architecture.core.runtime.configuration.runtimeModule
import io.architecture.data.repositoryModule
import io.architecture.datasource.api.LocalDataSource
import io.architecture.datasource.api.NetworkDataSource
import io.architecture.domain.di.useCaseModule
import io.architecture.network.websocket.imp.ktor.di.ktorClientModule
import io.architecture.network.websocket.imp.ktor.di.ktorModule
import io.architecture.network.websocket.imp.ktor.di.ktorServiceModule
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.test.verify.verify

class CoreKoinCheckModulesTest { // TODO Test fails due to kotlin-reflect. Too long (10 mins) to
                                 // sync Gradle project after deps changes (ktor module is slow down build speed)

    private val coreModule = module {
        includes(
            coroutineModule,
            runtimeModule,
            repositoryModule,
            useCaseModule,
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
                LocalDataSource::class, // Ignore. Provided in target module (:compose-app)
                NetworkDataSource::class, // Ignore. Provided in target module (:compose-app)
                kotlin.reflect.KClass::class,
                io.ktor.client.engine.HttpClientEngine::class,
                io.ktor.client.HttpClientConfig::class
            )
        )
    }
}