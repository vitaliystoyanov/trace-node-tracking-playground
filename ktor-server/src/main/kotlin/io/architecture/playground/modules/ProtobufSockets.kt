package io.architecture.playground.modules

import io.architecture.playground.model.NetworkClientTime
import io.architecture.playground.model.NetworkServerTime
import io.architecture.playground.service.sendRoutes
import io.architecture.playground.service.sendTraces
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.sendSerialized
import io.ktor.server.websocket.timeout
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readBytes
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import java.time.Duration

@OptIn(ExperimentalSerializationApi::class, DelicateCoroutinesApi::class)
fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
        @OptIn(ExperimentalSerializationApi::class)
        contentConverter = KotlinxWebsocketSerializationConverter(ProtoBuf)
    }
    routing {
        webSocket("/node/traces") {
            var sendingJob: Job? = null
            incoming.consumeAsFlow()
                .onStart { sendingJob = launch { sendTraces { sendSerialized(it) } } }
                .mapNotNull { it as? Frame.Binary }
                .map { ProtoBuf.decodeFromByteArray<NetworkClientTime>(it.readBytes()) }
                .onCompletion {
                    println("onCompletion invoked. cancelling sending coroutine job $sendingJob")
                    sendingJob?.cancel()
                }
                .collect {
                    println("client time is $it")
                    val response =
                        NetworkServerTime("rtt", it.time, System.currentTimeMillis())
                    println("server rtt respond  $response")
                    sendSerialized(response)
                }
        }
        webSocket("/nodes/route") {
            var sendingJob: Job? = null
            incoming.consumeAsFlow()
                .onStart { sendingJob = launch { sendRoutes { sendSerialized(it) } } }
                .onCompletion {
                    println("onCompletion invoked. cancelling sending coroutine job $sendingJob")
                    sendingJob?.cancel()
                }
                .collect {}
        }
    }
}
