package io.architecture.map

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val featureMapModule = module {
    viewModelOf(::MapViewModel)
}