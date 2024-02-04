package io.architecture.playground.data

import android.util.Log
import io.architecture.playground.data.local.DefaultLocalTraceDataSource
import io.architecture.playground.data.mapping.toExternal
import io.architecture.playground.data.mapping.toLocal
import io.architecture.playground.data.remote.DefaultNetworkTraceDataSource
import io.architecture.playground.data.remote.model.ConnectionState
import io.architecture.playground.data.remote.model.SocketConnectionState
import io.architecture.playground.model.Trace
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class DefaultTraceRepository @Inject constructor(
    private val networkDataSource: DefaultNetworkTraceDataSource,
    private val localDataSource: DefaultLocalTraceDataSource
) : TraceRepository {

    override fun getStreamConnectionState(): Flow<ConnectionState> =
        networkDataSource.observeConnection()
            .filter { it.type != SocketConnectionState.MESSAGE_RECEIVED }

    override fun getStreamTraces(): Flow<Trace> {
        return networkDataSource.streamTraces()
            .map { it.toExternal() }
            .onEach {
                localDataSource.add(it.toLocal())
            }
            .catch { error -> Log.d("REPOSITORY", "getStreamDiverTraces: error - $error") }
    }

    override fun getStreamCountTraces(): Flow<Long> {
        return localDataSource.observeCountTraces()
    }

    override fun deleteAllTraces() {
        localDataSource
    }

    override fun getStreamTraceHistory(): Flow<List<Trace>> {
        return localDataSource.observeAll()
            .map { it.toExternal() }
    }

    override fun getStreamLatestTraceByUniqNodeIds(): Flow<List<Trace>> {
        return localDataSource.observeLatestTraceByUniqNodeIds()
            .map { it.toExternal() }
    }

    override suspend fun getAllTracesByNodeId(nodeId: String): List<Trace> {
        return localDataSource.getAllTracesByNodeId(nodeId).map { it.toExternal() }
    }
}