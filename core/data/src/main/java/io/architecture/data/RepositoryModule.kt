package io.architecture.data

import io.architecture.data.repository.DefaultConnectionStateRepository
import io.architecture.data.repository.DefaultRouteRepository
import io.architecture.data.repository.DefaultTraceRepository
import io.architecture.data.repository.interfaces.DefaultNodeRepository
import org.koin.dsl.module

val repositoryModule = module {// todo binds
    factory { ::DefaultNodeRepository }
    factory { ::DefaultConnectionStateRepository }
    factory { ::DefaultRouteRepository }
    factory { ::DefaultTraceRepository }
}