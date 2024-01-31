package io.architecture.playground.data

import io.architecture.playground.data.local.DiverTraceDao
import io.architecture.playground.data.mapping.toExternal
import io.architecture.playground.data.mapping.toLocal
import io.architecture.playground.data.remote.NetworkDataSource
import io.architecture.playground.di.IoDispatcher
import io.architecture.playground.model.DiverTrace
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultDiverTraceRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val localDataSource: DiverTraceDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : DiverTraceRepository {

    override fun getStreamDiverTraces(): Flow<DiverTrace> {
        return  networkDataSource.streamDiverTraces()
            .map { it.toExternal() }
            .onEach { localDataSource.insert(it.toLocal()) }
    }

    override fun getStreamDiverTraceHistory(): Flow<List<DiverTrace>> {
        return localDataSource.observeAll().map { it.toExternal() }
    }

    override suspend fun removeAllTraces() {
        return localDataSource.deleteAll()
    }

}