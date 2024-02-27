import io.architecture.database.api.model.NodeEntity
import io.architecture.database.api.model.RouteEntity
import io.architecture.database.api.model.TraceEntity
import io.architecture.datasource.api.LocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

expect val appModule: Module

val mockLocalDatasourceModule = module { // TODO Provide mock in core module
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