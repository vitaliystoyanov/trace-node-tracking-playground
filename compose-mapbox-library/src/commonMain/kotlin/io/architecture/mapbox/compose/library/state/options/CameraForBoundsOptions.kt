package io.architecture.mapbox.compose.library.state.options

interface CameraForBoundsOptions : CameraOptions {
    // TODO   var offset: Point /* Point? | JsTuple<Number, Number> */
    var maxZoom: Number?
}