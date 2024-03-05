package io.architecture.core.runtime.configuration

data class Runtime(
    val websocketConfiguration: WebsocketRuntimeConfiguration,
    val platform: IPlatform,
    val clientProvider: WebsocketClientProvider,
)