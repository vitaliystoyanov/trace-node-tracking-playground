package io.architecture.mapbox.compose.library

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ca.derekellis.mapbox.MapboxMapInternal
import ca.derekellis.mapbox.rememberMapboxStateInternal
import io.architecture.mapbox.compose.library.state.MapboxState
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.css.vw
import org.jetbrains.compose.web.css.width

@Composable
actual fun MapBoxMap(
    modifier: Modifier,
    accessToken: String,
    state: MapboxState,
    style: String,
    content: @MapboxMapComposableMarker @Composable (MapboxMapScope.() -> Unit)?,
) {
    MapboxMapInternal(
        accessToken = accessToken,
        style = style,
        state = rememberMapboxStateInternal(
            zoom = state.zoom,
            pitch = state.pitch,
            bearing = state.bearing,
            center = LngLat(state.center.longitude, state.center.latitude),
        ),
        containerAttrs = {
            style {
                height(100.vh)
                width(100.vw)
            }
        },
    ) {

    }
}