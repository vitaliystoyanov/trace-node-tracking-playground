package io.architecture.playground

import io.architecture.playground.model.NetworkClientTime
import io.architecture.playground.modules.configureSockets
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToHexString
import kotlinx.serialization.protobuf.ProtoBuf

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

@OptIn(ExperimentalSerializationApi::class)
fun Application.module() {
    configureSockets()

    println(
        "Test binary message for NetworkClientTime:  ${
            ProtoBuf.encodeToHexString<NetworkClientTime>(
                NetworkClientTime(System.currentTimeMillis())
            )
        }"
    )
}