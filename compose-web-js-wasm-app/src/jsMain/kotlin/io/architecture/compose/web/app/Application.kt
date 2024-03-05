package io.architecture.compose.web.app

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import ca.derekellis.mapbox.MapboxMapInternal
import ca.derekellis.mapbox.rememberMapboxStateInternal
import io.architecture.compose.web.application.BuildKonfig
import io.architecture.core.design.system.theme.UAVTheme
import io.architecture.core.di.coreKoinModules
import io.architecture.datasource.api.di.inMemoryLocalDatasourceModule
import io.architecture.feature.common.map.MapScreen
import io.architecture.feature.common.map.di.mapFeatureModule
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.renderComposable
import org.jetbrains.skiko.wasm.onWasmReady
import org.koin.compose.KoinApplication

external fun require(module: String): dynamic

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    require("mapbox-gl/dist/mapbox-gl.css")

    onWasmReady {
        CanvasBasedWindow(
            title = "Node trace tracking",
            canvasElementId = "composeTarget",
            applyDefaultStyles = false
        ) {
            KoinApplication(application = {
                modules(coreKoinModules, mapFeatureModule, inMemoryLocalDatasourceModule)
            }) {
                UAVTheme {
                    MapScreen()
                }
            }
        }
    }

    renderComposable(rootElementId = "root") {
        Style(MainStyleSheet)
        Div(attrs = { classes(MainStyleSheet.overlay) }) {
            Div {
                val mapState = rememberMapboxStateInternal()
                MapboxMapInternal(
                    accessToken = BuildKonfig.MAPBOX_ACCESS_TOKEN,
                    style = "mapbox://styles/mapbox/dark-v10",
                    state = mapState,
                ) {
                    // TODO GeoJson source...
                }
            }
        }
    }
}