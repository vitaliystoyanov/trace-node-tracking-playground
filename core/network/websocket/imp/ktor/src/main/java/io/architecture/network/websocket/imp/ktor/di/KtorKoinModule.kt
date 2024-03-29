package io.architecture.network.websocket.imp.ktor.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
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
        HttpClient(CIO) {

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