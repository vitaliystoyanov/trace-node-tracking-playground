package io.architecture.playground.data

import android.util.Log
import io.architecture.playground.data.local.DefaultLocalDiverTraceDataSource
import io.architecture.playground.data.mapping.toExternal
import io.architecture.playground.data.mapping.toLocal
import io.architecture.playground.data.remote.DefaultNetworkDiverTraceDataSource
import io.architecture.playground.data.remote.model.NetworkConnectionEvent
import io.architecture.playground.data.remote.model.NetworkConnectionEventType
import io.architecture.playground.di.IoDispatcher
import io.architecture.playground.model.DiverTrace
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

class DefaultDiverTraceRepository @Inject constructor(
    private val networkDataSource: DefaultNetworkDiverTraceDataSource,
    private val localDataSource: DefaultLocalDiverTraceDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : DiverTraceRepository {

    private val coroutineScope = CoroutineScope(ioDispatcher)
    private var cacheTraces = mutableListOf<DiverTrace>()

    override fun observeConnection(): Flow<NetworkConnectionEvent> =
        networkDataSource.observeConnection()
            .filter { it.type != NetworkConnectionEventType.MessageReceived }

    override fun getStreamDiverTraces(): Flow<DiverTrace> {
        return networkDataSource.streamDiverTraces()
            .map { it.toExternal() }
            .onEach {
                localDataSource.add(it.toLocal())
            }
            .catch { error -> Log.d("REPOSITORY", "getStreamDiverTraces: error - $error") }
    }

    override fun getStreamDiverTraceHistory(): Flow<List<DiverTrace>> {
        coroutineScope.launch {
            localDataSource.getAll()
                .map { it.toExternal() }
                .also {
                    cacheTraces = it.toMutableList()
                }
        }
        return localDataSource.observeAll()
            .map { it.toExternal() }
            .onEach { cacheTraces.add(it.last()) }
            .transform { emit(cacheTraces) }
    }
}