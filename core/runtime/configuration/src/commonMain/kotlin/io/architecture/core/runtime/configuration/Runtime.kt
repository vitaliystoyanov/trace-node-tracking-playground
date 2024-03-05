package io.architecture.core.runtime.configuration

data class Runtime(
    val websocketConfiguration: WebsocketRuntimeConfiguration,
    val platform: IPlatform,
    val clientProvider: WebsocketClientProvider,
    var httpAgent: String
) {
    init {
        validateHttpAgent()
    }

    private fun validateHttpAgent() = httpAgent.isNotBlank()
}