package io.architecture.playground.data.repository

import android.util.Log
import io.architecture.playground.data.mapping.toExternalAs
import io.architecture.playground.data.remote.NetworkDataSource
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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class DefaultConnectionStateRepository @Inject constructor(
    @ApplicationScope private val applicationScope: CoroutineScope,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val network: NetworkDataSource,
) : ConnectionStateRepository {

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
            Log.d("RTT_NETWORK", "sendClientTimeWith: next frame of client time is $interval")
            network.sendClientTime(NetworkClientTime(System.currentTimeMillis()))
        } catch (exception: Exception) {
            Log.e("RTT_NETWORK", "sendClientTimeWith: error -> ", exception)
        }
    }

    override fun streamConnectionEvents(): Flow<Connection> =
        network.streamConnectionEvents()
            .map { it.toExternalAs() }
}