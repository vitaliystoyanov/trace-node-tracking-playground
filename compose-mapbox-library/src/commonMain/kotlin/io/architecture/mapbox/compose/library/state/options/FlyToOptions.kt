package io.architecture.mapbox.compose.library.state.options

interface FlyToOptions : AnimationOptions, CameraOptions {
    var curve: Number?
    var minZoom: Number?
    var speed: Number?
    var screenSpeed: Number?
    var maxDuration: Number?
}