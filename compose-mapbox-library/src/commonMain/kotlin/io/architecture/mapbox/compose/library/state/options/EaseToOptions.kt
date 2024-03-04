package io.architecture.mapbox.compose.library.state.options

interface EaseToOptions : AnimationOptions, CameraOptions {
    var delayEndEvents: Number?
}