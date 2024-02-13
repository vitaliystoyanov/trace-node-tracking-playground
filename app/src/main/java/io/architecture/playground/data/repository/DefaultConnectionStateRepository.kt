package io.architecture.playground.data.repository

import android.util.Log
import io.architecture.playground.data.remote.interfaces.NetworkDataSource
import io.architecture.playground.data.remote.model.ConnectionEvent
import io.architecture.playground.data.remote.model.NetworkClientTime
import io.architecture.playground.data.repository.interfaces.ConnectionStateRepository
import io.architecture.playground.di.ApplicationScope
import io.architecture.playground.di.IoDispatcher
import io.architecture.playground.model.Connection
import io.architecture.playground.model.UpstreamRtt
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class DefaultConnectionStateRepository @Inject constructor(
    @ApplicationScope private val applicationScope: CoroutineScope,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val network: NetworkDataSource,
) : ConnectionStateRepository {

    override val connectionStateShared: SharedFlow<Map<Int, Connection>> = combine(
        combine(
            streamTraceConnectionEvents(),
            streamRoundTripTime(100.milliseconds)
        ) { connectionEvent, upstreamRtt ->
            Connection(connectionEvent, upstreamRtt)
        },
        combine(
            streamRouteConnectionEvents(),
            flow { emit(UpstreamRtt(0)) } // TODO Dummy flow
        ) { connectionEvent, upstreamRtt ->
            Connection(connectionEvent, upstreamRtt)
        },
    ) { trace, route ->
        mapOf(
            Connection.TRACE_SERVICE_CONNECTION to trace,
            Connection.ROUTE_SERVICE_CONNECTION to route
        )
    }.shareIn(
        applicationScope,
        replay = 1,
        started = SharingStarted.WhileSubscribed()
    )

    private val skipMessageReceived =
        { event: ConnectionEvent -> event.let { event != ConnectionEvent.MESSAGE_RECEIVED } }

    override fun streamRoundTripTime(interval: Duration): Flow<UpstreamRtt> =
        network.streamServerTime()
            .onStart { sendClientTimeWith(2.seconds) } // TODO Start on connection
            .onEach { sendClientTimeWith(interval) }
            .map { serverTime -> UpstreamRtt(System.currentTimeMillis() - serverTime.clientSentTime) }
            .catch { error -> Log.e("RTT_NETWORK", "streamRoundTripTime ", error) }
            .flowOn(ioDispatcher)

    private suspend fun sendClientTimeWith(interval: Duration) {
        delay(interval)
        try {
            network.sendClientTime(NetworkClientTime(System.currentTimeMillis()))
        } catch (exception: Exception) {
            Log.e("RTT_NETWORK", "sendClientTimeWith: ", exception)
        }
    }

    override fun streamTraceConnectionEvents(): Flow<ConnectionEvent> =
        network.streamTraceConnectionState()
//             .filter(skipMessageReceived)

    override fun streamRouteConnectionEvents(): Flow<ConnectionEvent> =
        network.streamRouteConnectionState()
// TODO .filter(skipMessageReceived) Throws null pointer exception
}