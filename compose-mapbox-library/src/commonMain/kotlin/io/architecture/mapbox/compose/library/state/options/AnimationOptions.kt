package io.architecture.mapbox.compose.library.state.options

interface AnimationOptions {
    var duration: Number?
    var easing: ((time: Number) -> Number)?
// TODO   var offset: dynamic /* Point? | JsTuple<Number, Number> */
    var animate: Boolean?
    var essential: Boolean?
}