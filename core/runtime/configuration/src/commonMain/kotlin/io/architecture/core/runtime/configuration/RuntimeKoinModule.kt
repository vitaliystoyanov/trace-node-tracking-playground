package io.architecture.core.runtime.configuration

import org.koin.dsl.module

val runtimeModule = module {
    single { WebsocketRuntimeConfiguration() }
    single {
        Runtime(
            websocketConfiguration = get<WebsocketRuntimeConfiguration>(),
            platform = platform,
            clientProvider = WebsocketClientProvider.KTOR_PROTOBUF,
            httpAgent = "undefined"
        )
    }
}