package io.architecture.domain

import org.koin.dsl.module

// Workaround for compile error -> Type org.koin.ksp.generated.DefaultKt$defaultModule$1$1 is defined multiple times:
// [https://github.com/InsertKoinIO/koin-annotations/issues/83]
val useCaseModule = module {
    factory { ::ObserveAndStoreTracesUseCase }
    factory { ::ObserveAndStoreRoutesUseCase }
    factory { ::GetStreamTraceUseCase }
    factory { ::GetStreamChunkedNodeWithTraceUseCase }
    factory { ::GetConnectionStateUseCase }
    factory { ::FormatDatetimeUseCase }
    factory { ::ConvertAzimuthToDirectionUseCase }
}