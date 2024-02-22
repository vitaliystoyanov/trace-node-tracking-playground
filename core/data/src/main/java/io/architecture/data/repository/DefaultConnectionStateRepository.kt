package io.architecture.data.repository

import android.util.Log
import io.architecture.data.repository.interfaces.ConnectionStateRepository
import io.architecture.datasource.api.NetworkDataSource
import io.architecture.model.Connection
import io.architecture.model.ConnectionEvent
import io.architecture.model.UpstreamRtt
import io.architecture.network.websocket.api.model.NetworkClientTime
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class DefaultConnectionStateRepository(
    @Named("applicationScope") private val applicationScope: CoroutineScope,
    @Named("ioDispatcher") private val ioDispatcher: CoroutineDispatcher,
    private val network: NetworkDataSource,
) : ConnectionStateRepository {

    private val skipMessageReceived =
        { event: ConnectionEvent -> event.let { event != ConnectionEvent.MESSAGE_RECEIVED } }

    override fun streamRoundTripTime(interval: Duration): Flow<UpstreamRtt> =
        network.streamServerTime()
            .onStart {
                Log.d(
                    "RTT_NETWORK",
                    "streamRoundTripTime: RTT: sending the first client time..."
                )
                sendClientTimeWith(2.seconds) // TODO Start on connection opened state
            }
            .map { serverTime -> UpstreamRtt(System.currentTimeMillis() - serverTime.clientSentTime) }
            .onEach { sendClientTimeWith(interval) }
            .catch { error -> Log.e("RTT_NETWORK", "streamRoundTripTime ", error) }
            .flowOn(ioDispatcher)

    private suspend fun sendClientTimeWith(interval: Duration) {
        delay(interval)
        try {
            network.sendClientTime(NetworkClientTime(System.currentTimeMillis()))
        } catch (exception: Exception) {
            Log.e("RTT_NETWORK", "sendClientTimeWith: error -> ", exception)
        }
    }

    override fun streamConnectionEvents(): SharedFlow<ConnectionEvent> {
        Log.d(
            "RTT_NETWORK",
            "streamConnectionEvents: Connection events to replay connection events on collect -> " +
                    "${network.streamConnectionEvents().replayCache.size}"
        )
        return network.streamConnectionEvents()
    }
}

fun ConnectionEvent.toExternal(): Connection =
    Connection(
        state = toExternalState(),
        rtt = UpstreamRtt(0L)
    )

internal fun ConnectionEvent.toExternalState(): Connection.State = when (this) {
    ConnectionEvent.UNDEFINED -> Connection.State.UNDEFINED
    ConnectionEvent.OPENED -> Connection.State.OPENED
    ConnectionEvent.CLOSED -> Connection.State.CLOSED
    ConnectionEvent.CLOSING -> Connection.State.CLOSING
    ConnectionEvent.FAILED -> Connection.State.FAILED
    ConnectionEvent.MESSAGE_RECEIVED -> Connection.State.MESSAGE_RECEIVED
}