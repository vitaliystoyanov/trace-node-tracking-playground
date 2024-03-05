package io.architecture.network.websocket.imp.ktor.di

import io.architecture.core.runtime.configuration.Runtime
import io.ktor.client.HttpClient
import io.ktor.client.plugins.UserAgent
// todo import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.protobuf.ProtoBuf
import org.koin.dsl.bind
import org.koin.dsl.module
import io.architecture.runtime.logging.Logger as LoggerInternal

val ktorModule = module {
    includes(ktorServiceModule)

    @OptIn(ExperimentalSerializationApi::class)
    single {
        // NOTE: If you call the HttpClient constructor without an argument, the client will choose
        // an engine automatically depending on the artifacts added in a build script.
        // https://ktor.io/docs/http-client-engines.html#default
        HttpClient {

            install(UserAgent) {
                agent = get<Runtime>().httpAgent
            }

            install(WebSockets) {
                contentConverter = KotlinxWebsocketSerializationConverter(ProtoBuf)
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        LoggerInternal.debug("KTOR_SERVICE", message)
                    }
                }
                level = LogLevel.ALL
            }
        }
    } bind HttpClient::class
}