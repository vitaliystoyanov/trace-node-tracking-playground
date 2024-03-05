package io.architecture.core.di

import io.architecture.database.api.model.NodeEntity
import io.architecture.database.api.model.RouteEntity
import io.architecture.database.api.model.TraceEntity
import io.architecture.datasource.api.LocalDataSource
import io.architecture.datasource.api.NetworkDataSource
import io.architecture.model.ConnectionEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.emptyFlow
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.test.check.checkModules
import kotlin.test.Test

class CoreKoinCheckModulesTest {

    @Test
    fun checkKoinModule() {

        val mockModules = module {
            single {
                object : NetworkDataSource {
                    override fun openSession() {}
                    override fun closeSession() {}
                    override fun streamTraces(): Flow<io.architecture.network.websocket.api.model.NetworkTrace> = emptyFlow()
                    override fun streamRoutes(): Flow<io.architecture.network.websocket.api.model.NetworkRoute> = emptyFlow()
                    override suspend fun sendClientTime(time: io.architecture.network.websocket.api.model.NetworkClientTime) {}
                    override fun streamServerTime(): Flow<io.architecture.network.websocket.api.model.NetworkServerTime> = emptyFlow()
                    override fun streamConnectionEvents(): SharedFlow<ConnectionEvent> = emptyFlow<ConnectionEvent>() as SharedFlow<ConnectionEvent>
                }
            } bind NetworkDataSource::class
            single {
                object : LocalDataSource {
                    override fun observeAllTraces(): Flow<List<TraceEntity>> = emptyFlow()
                    override fun observeAllNodes(): Flow<List<NodeEntity>> = emptyFlow()
                    override fun observeTraceCount(): Flow<Int> = emptyFlow()
                    override fun observeTraceBy(nodeId: String): Flow<TraceEntity> = emptyFlow()
                    override fun observeNodeCount(): Flow<Int> = emptyFlow()
                    override suspend fun getRouteBy(nodeId: String): RouteEntity? = null
                    override suspend fun deleteAllTraces() {}
                    override suspend fun createOrUpdate(trace: TraceEntity) {}
                    override suspend fun createOrUpdate(route: RouteEntity) {}
                    override suspend fun createOrUpdate(node: NodeEntity) {}
                }
            } bind LocalDataSource::class
        }

        checkModules {
            modules(coreKoinModules, mockModules)
        }
    }
}