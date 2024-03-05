package io.architecture.mapbox.compose.library.state.options

import io.architecture.mapbox.compose.library.LngLat

interface CameraOptions {
    var center: LngLat?  /* JsTuple<Number, Number> | LngLat? | `T$1`? | `T$2`? */
    var zoom: Number?
    var bearing: Number?
    var pitch: Number?
    var around: LngLat? /* JsTuple<Number, Number> | LngLat? | `T$1`? | `T$2`? */
    var padding: PaddingOptions? /* Number? | PaddingOptions? */
}