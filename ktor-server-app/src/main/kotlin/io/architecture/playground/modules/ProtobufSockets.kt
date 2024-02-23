package io.architecture.playground.modules

import io.architecture.network.websocket.api.model.NetworkClientTime
import io.architecture.network.websocket.api.model.NetworkServerTime
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
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.protobuf.ProtoBuf
import kotlinx.serialization.protobuf.ProtoBuf.Default.decodeFromByteArray
import java.time.Duration

@OptIn(ExperimentalSerializationApi::class)
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
        webSocket("/nodes/traces") {
            try {
                sendTraces {
                    sendSerialized(it)
                }
            } catch (e: ClosedReceiveChannelException) {
                LOGGER.error("/nodes/traces onError -> ${closeReason.await()}", e)
            } catch (e: Throwable) {
                LOGGER.error("/nodes/traces onError -> ${closeReason.await()}", e)
            }
        }

        webSocket("/nodes/routes") {
            try {
                sendRoutes { sendSerialized(it) }
            } catch (e: ClosedReceiveChannelException) {
                LOGGER.error("/nodes/routes onError -> ${closeReason.await()}", e)
            } catch (e: Throwable) {
                LOGGER.error("/nodes/routes onError -> ${closeReason.await()}", e)
            }
        }

        webSocket("/rtt") {
            incoming.consumeAsFlow()
                .mapNotNull { it as? Frame.Binary }
                .map {
                    decodeFromByteArray(
                        NetworkClientTime.serializer(),
                        it.readBytes()
                    )
                } // Workaround. Instead of receiveSerialized(...)
                .catch {  LOGGER.error("/rtt onError -> ${closeReason.await()}", it) }
                .collect {
                    LOGGER.info("RTT: client time is $it")

                    val response = NetworkServerTime("rtt", it.time, System.currentTimeMillis())
                    LOGGER.info("RTT: server rtt respond  $response")
                    sendSerialized(response)
                }
        }
    }
}
