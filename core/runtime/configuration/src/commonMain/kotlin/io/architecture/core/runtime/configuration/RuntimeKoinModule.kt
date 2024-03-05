package io.architecture.core.runtime.configuration

import org.koin.dsl.module

val runtimeModule = module {
    single { WebsocketRuntimeConfiguration() }
    single {
        Runtime(
            get<WebsocketRuntimeConfiguration>(),
            platform,
            WebsocketClientProvider.KTOR_PROTOBUF
        )
    }
}