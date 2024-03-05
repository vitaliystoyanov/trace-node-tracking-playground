package io.architecture.mapbox.compose.library

import androidx.compose.runtime.ComposableTargetMarker

@Retention(AnnotationRetention.BINARY)
@ComposableTargetMarker(description = "MapBox Composable")
@Target(
    AnnotationTarget.FILE,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.TYPE,
    AnnotationTarget.TYPE_PARAMETER,
)
public annotation class MapboxMapComposableMarker
