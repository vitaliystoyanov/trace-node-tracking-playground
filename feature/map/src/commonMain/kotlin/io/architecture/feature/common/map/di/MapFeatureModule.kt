package io.architecture.feature.common.map.di

import io.architecture.data.repository.interfaces.NodeRepository
import io.architecture.data.repository.interfaces.RouteRepository
import io.architecture.domain.GetConnectionStateUseCase
import io.architecture.domain.GetStreamChunkedNodeWithTraceUseCase
import io.architecture.domain.GetStreamTraceByIdUseCase
import io.architecture.feature.common.map.MapViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mapFeatureModule = module {
    single {
        MapViewModel(
            get<CoroutineScope>(named("applicationScope")),
            get<GetStreamTraceByIdUseCase>(),
            get<RouteRepository>(),
            get<GetStreamChunkedNodeWithTraceUseCase>(),
            get<GetConnectionStateUseCase>(),
            get<NodeRepository>()
        )
    }
}