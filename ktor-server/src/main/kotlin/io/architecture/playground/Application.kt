package io.architecture.playground

import io.architecture.playground.model.NetworkClientTime
import io.architecture.playground.modules.LOGGER
import io.architecture.playground.modules.configureSockets
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToHexString
import kotlinx.serialization.protobuf.ProtoBuf
import org.slf4j.event.Level

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

@OptIn(ExperimentalSerializationApi::class)
fun Application.module() {
    configureSockets()
    install(CallLogging) {
        level = Level.INFO
        filter { call ->
            call.request.path().startsWith("/")
        }
        format { call ->
            val status = call.response.status()
            val httpMethod = call.request.httpMethod.value
            val userAgent = call.request.headers["User-Agent"]
            "Status: $status, HTTP method: $httpMethod, User agent: $userAgent"
        }
    }
    LOGGER.debug(
        "Test binary message for NetworkClientTime:  ${
            ProtoBuf.encodeToHexString<NetworkClientTime>(
                NetworkClientTime(System.currentTimeMillis())
            )
        }"
    )
}