package io.architecture.mapbox.compose.library.state

import io.architecture.mapbox.compose.library.LngLat
import io.architecture.mapbox.compose.library.LngLatBounds
import io.architecture.mapbox.compose.library.state.options.AnimationOptions
import io.architecture.mapbox.compose.library.state.options.CameraOptions
import io.architecture.mapbox.compose.library.state.options.FitBoundsOptions
import io.architecture.mapbox.compose.library.state.options.PaddingOptions
import kotlinx.coroutines.suspendCancellableCoroutine

class MapboxState(
    internal val initialCenter: LngLat = LngLat(0.0, 0.0),
    internal val initialZoom: Double = 0.0,
    internal val initialBearing: Double = 0.0,
    internal val initialPitch: Double = 0.0,
) {

    var center: LngLat = LngLat(0.0, 0.0)

    var zoom: Double = 0.0

    var bearing: Double = 0.0

    var padding: PaddingOptions = object : PaddingOptions {
        override var top: Number
            get() = 0
            set(value) {} // tODO
        override var bottom: Number
            get() = 0
            set(value) {}
        override var left: Number
            get() = 0
            set(value) {}
        override var right: Number
            get() = 0
            set(value) {}

    }

    var pitch: Double = 0.0

    suspend fun panBy(x: Double, y: Double, options: (AnimationOptions.() -> Unit)? = null) =
        doMoveAnimation {

        }

    suspend fun panTo(location: LngLat, options: (AnimationOptions.() -> Unit)? = null) =
        doMoveAnimation {

        }

    suspend fun zoomTo(zoom: Double, options: (AnimationOptions.() -> Unit)? = null) =
        doMoveAnimation {

        }

    suspend fun zoomIn(options: (AnimationOptions.() -> Unit)? = null) = doMoveAnimation {

    }

    suspend fun zoomOut(options: (AnimationOptions.() -> Unit)? = null) = doMoveAnimation {

    }

    suspend fun rotateTo(bearing: Double, options: (AnimationOptions.() -> Unit)? = null) =
        doMoveAnimation {

        }

    suspend fun resetNorth(options: (AnimationOptions.() -> Unit)? = null) = doMoveAnimation {

    }

    suspend fun resetNorthPitch(options: (AnimationOptions.() -> Unit)? = null) = doMoveAnimation {

    }

    suspend fun snapToNorth(options: (AnimationOptions.() -> Unit)? = null) = doMoveAnimation {

    }

    fun cameraForBounds(
        bounds: LngLatBounds,
        options: (CameraOptions.() -> Unit)? = null,
    ): CameraOptions? {
        return null
    }

    suspend fun fitBounds(bounds: LngLatBounds, options: (FitBoundsOptions.() -> Unit)? = null) =
        doMoveAnimation {

        }

    suspend fun easeTo(
        center: LngLat = this.center,
        zoom: Double = this.zoom,
        bearing: Double = this.bearing,
        pitch: Double = this.pitch,
        padding: PaddingOptions = this.padding,
        options: (AnimationOptions.() -> Unit)? = null,
    ) = doMoveAnimation {

    }

    suspend fun flyTo(
        center: LngLat = this.center,
        zoom: Double = this.zoom,
        bearing: Double = this.bearing,
        pitch: Double = this.pitch,
        padding: PaddingOptions = this.padding,
        options: (AnimationOptions.() -> Unit)? = null,
    ) = doMoveAnimation {

    }

    /**
     * Runs the given [block], assuming that the [block] triggers a move animation on the [map].
     * Suspends until the move animation has ended.
     */
    private suspend fun doMoveAnimation(block: () -> Unit) {
        var listener: (Any) -> Unit
        var resumed = false
        suspendCancellableCoroutine<Unit> { continuation ->
            block()

        }
    }
}
