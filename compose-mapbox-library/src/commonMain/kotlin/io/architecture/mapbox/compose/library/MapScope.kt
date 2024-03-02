package io.architecture.mapbox.compose.library

import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope

open class MapScope() {

    @Composable
    fun GeoJsonSource(
        id: String,
        geojson: String,
    ) {

    }
}

fun CoroutineScope.applySources(content: @Composable MapScope.() -> Unit) {

}