package io.architecture.shared

import androidx.compose.ui.window.ComposeUIViewController
import io.architecture.core.di.coreKoinModules
import io.architecture.datasource.api.emptyMockLocalDatasourceModule
import io.architecture.feature.common.map.MapScreen
import io.architecture.feature.common.map.di.mapFeatureModule
import org.koin.compose.KoinApplication
import platform.UIKit.UIViewController

fun controller(): UIViewController = ComposeUIViewController {
    KoinApplication(application = {
        modules(coreKoinModules, mapFeatureModule, emptyMockLocalDatasourceModule)
    }) {
        MapScreen()
    }
}