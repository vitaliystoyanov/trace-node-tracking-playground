package io.architecture.mapbox.compose.library.state.options

interface FitBoundsOptions : FlyToOptions {
    var linear: Boolean?
// TODO    override var offset: dynamic /* Point? | JsTuple<Number, Number> */
    var maxZoom: Number?
    override var maxDuration: Number?
}