package io.architecture.domain.di

import io.architecture.data.repository.interfaces.ConnectionStateRepository
import io.architecture.data.repository.interfaces.NodeRepository
import io.architecture.data.repository.interfaces.RouteRepository
import io.architecture.data.repository.interfaces.TraceRepository
import io.architecture.domain.ConvertAzimuthToDirectionUseCase
import io.architecture.domain.FormatDatetimeUseCase
import io.architecture.domain.GetConnectionStateUseCase
import io.architecture.domain.GetStreamChunkedNodeWithTraceUseCase
import io.architecture.domain.GetStreamTraceByIdUseCase
import io.architecture.domain.PersistRoutesUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

// Workaround for compile error -> Type org.koin.ksp.generated.DefaultKt$defaultModule$1$1 is defined multiple times:
// [https://github.com/InsertKoinIO/koin-annotations/issues/83]
val useCaseModule = module {
    factoryOf(::FormatDatetimeUseCase)
    factoryOf(::ConvertAzimuthToDirectionUseCase)

    single {
        PersistRoutesUseCase(
            get<RouteRepository>(),
            get(named("defaultDispatcher")),
            get(named("ioDispatcher")),
        )
    }

    single {
        GetStreamTraceByIdUseCase(
            get<TraceRepository>(),
            get<FormatDatetimeUseCase>(),
            get<ConvertAzimuthToDirectionUseCase>(),
            get(named("defaultDispatcher")),
            get(named("ioDispatcher")),
        )
    }

    single {
        GetStreamChunkedNodeWithTraceUseCase(
            get<NodeRepository>(),
            get<TraceRepository>(),
            get<FormatDatetimeUseCase>(),
            get<ConvertAzimuthToDirectionUseCase>(),
            get(named("defaultDispatcher")),
            get(named("ioDispatcher")),
        )
    }

    single {
        GetConnectionStateUseCase(
            get<ConnectionStateRepository>(),
            get(named("defaultDispatcher")),
            get(named("ioDispatcher")),
        )
    }
}