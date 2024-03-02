package io.architecture.mapbox.compose.library

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.architecture.mapbox.compose.library.state.MapboxState

@Composable
actual fun MapBoxMap(
    modifier: Modifier,
    accessToken: String,
    state: MapboxState,
    style: String,
    content: @MapboxMapComposableMarker @Composable (MapboxMapScope.() -> Unit)?,
) {
}