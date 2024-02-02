package io.architecture.playground.data

import android.util.Log
import io.architecture.playground.data.local.DefaultLocalTraceDataSource
import io.architecture.playground.data.mapping.toExternal
import io.architecture.playground.data.mapping.toLocal
import io.architecture.playground.data.remote.DefaultNetworkTraceDataSource
import io.architecture.playground.data.remote.model.NetworkConnectionEvent
import io.architecture.playground.data.remote.model.NetworkConnectionEventType
import io.architecture.playground.di.IoDispatcher
import io.architecture.playground.model.Trace
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import javax.inject.Inject

class DefaultTraceRepository @Inject constructor(
    private val networkDataSource: DefaultNetworkTraceDataSource,
    private val localDataSource: DefaultLocalTraceDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : TraceRepository {

    private val coroutineScope = CoroutineScope(ioDispatcher)
    private var cacheTraces = mutableListOf<Trace>()

    override fun getStreamConnectionEvents(): Flow<NetworkConnectionEvent> =
        networkDataSource.observeConnection()
            .filter { it.type != NetworkConnectionEventType.MessageReceived }

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
        coroutineScope.launch {
            localDataSource.getAll()
                .map { it.toExternal() }
                .also {
                    cacheTraces = it.toMutableList()
                }
        }
        return localDataSource.observeAll()
            .map { it.toExternal() }
            .onEach { if (it.lastOrNull() != null) cacheTraces.add(it.last()) }
            .transform { emit(cacheTraces) }
    }

    override fun getStreamLatestTraceByUniqNodeIds(): Flow<List<Trace>> {
        return localDataSource.observeLatestTraceByUniqNodeIds()
            .map { it.toExternal() }
//            .onEach { if (it.lastOrNull() != null) cacheTraces.add(it.last()) }
//            .transform { emit(cacheTraces) }
    }
}