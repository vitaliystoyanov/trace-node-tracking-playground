package io.architecture.data.repository

import android.util.Log
import io.architecture.common.ApplicationScope
import io.architecture.common.IoDispatcher
import io.architecture.data.mapping.toExternal
import io.architecture.data.repository.interfaces.ConnectionStateRepository
import io.architecture.datasource.api.NetworkDataSource
import io.architecture.model.Connection
import io.architecture.network.websocket.api.model.NetworkClientTime
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
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
        { event: io.architecture.model.ConnectionEvent -> event.let { event != io.architecture.model.ConnectionEvent.MESSAGE_RECEIVED } }

    override fun streamRoundTripTime(interval: Duration): Flow<io.architecture.model.UpstreamRtt> =
        network.streamServerTime()
            .onStart { sendClientTimeWith(2.seconds) } // TODO Start on connection
            .onEach { sendClientTimeWith(interval) }
            .map { serverTime -> io.architecture.model.UpstreamRtt(System.currentTimeMillis() - serverTime.clientSentTime) }
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
            .map { it.toExternal() }
}