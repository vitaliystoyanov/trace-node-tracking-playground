package io.architecture.mapbox.compose.library

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import io.architecture.mapbox.compose.library.state.MapboxState

@Composable
expect fun MapBoxMap(
    modifier: Modifier = Modifier,
    accessToken: String,
    state: MapboxState,
    style: String,
//    onMapClickListener: OnMapClickListener,
    content: (@Composable @MapboxMapComposableMarker MapboxMapScope.() -> Unit)? = null,
)

@Composable
fun rememberMapboxState(
    center: LngLat = LngLat(0.0, 0.0),
    zoom: Double = 0.0,
    bearing: Double = 0.0,
    pitch: Double = 0.0,
): MapboxState {
    return remember { MapboxState(center, zoom, bearing, pitch) }
}