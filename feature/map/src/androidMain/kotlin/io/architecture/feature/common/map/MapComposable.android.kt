package io.architecture.feature.common.map

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.RenderedQueryGeometry
import com.mapbox.maps.RenderedQueryOptions
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource
import com.mapbox.maps.extension.style.sources.getSource
import com.mapbox.maps.plugin.compass.generated.CompassSettings
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.gestures.generated.GesturesSettings
import com.mapbox.maps.plugin.scalebar.generated.ScaleBarSettings
import io.architecture.model.Route
import io.architecture.model.Trace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.measureTime

@OptIn(MapboxExperimental::class)
@Composable
actual fun MapComposable(
    padding: PaddingValues,
    nodeTraces: Sequence<Trace>,
    displayRoute: Route?,
    onNodeClick: (String) -> Unit,
) {

    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            center(
                Point.fromLngLat(
                    MapInitialState.CAMERA_INITIAL_POINT_LNG,
                    MapInitialState.CAMERA_INITIAL_POINT_LAT
                )
            )
            zoom(MapInitialState.CAMERA_INITIAL_ZOOM)
            pitch(MapInitialState.CAMERA_INITIAL_PITCH)
            bearing(MapInitialState.CAMERA_INITIAL_BEARING)
        }
    }
    val compassSettings: CompassSettings by remember {
        mutableStateOf(CompassSettings { enabled = false })
    }
    val scaleBarSetting: ScaleBarSettings by remember {
        mutableStateOf(ScaleBarSettings { enabled = false })
    }
    val gesturesSettings: GesturesSettings by remember {
        mutableStateOf(GesturesSettings { rotateEnabled = false })
    }

    MapboxMap(
        Modifier.padding(padding),
        mapInitOptionsFactory = { context -> MapInitOptions(context) },
        mapViewportState = mapViewportState,
        compassSettings = compassSettings,
        scaleBarSettings = scaleBarSetting,
        gesturesSettings = gesturesSettings,
    ) {
        MapEffect(Unit) { view ->
            with(view.mapboxMap) {
                loadStyle(createStyle(view.context))
                addOnMapClickListener { point ->
                    queryRenderedFeatures(
                        RenderedQueryGeometry(pixelForCoordinate(point)),
                        RenderedQueryOptions(
                            listOf(
                                LAYER_SYMBOL_ID,
                                LAYER_CIRCLE_ID
                            ), null
                        )
                    ) {
                        it.value?.forEach { renderedFeature ->
                            onNodeClick(
                                renderedFeature.queriedFeature.feature.getStringProperty(
                                    NODE_ID_KEY_PROPERTY
                                )
                            )
                        }
                    }
                    true
                }
            }
        }

        MapEffect(nodeTraces) { view ->
            val source = view.mapboxMap.getSource(NODE_DATA_SOURCE_ID) as? GeoJsonSource
            val features = mutableListOf<Feature>() // TODO

            withContext(Dispatchers.Default) {
                generateFeatures(nodeTraces, features, displayRoute)
            }
            val timeAddToFeatureCollection = measureTime {
                source?.featureCollection(FeatureCollection.fromFeatures(features))
            }
            Log.d(
                "RENDER_DEBUG",
                "Time to add into feature collection - " +
                        "${timeAddToFeatureCollection.inWholeMilliseconds} ms"
            )
        }
    }
}

private fun generateFeatures(
    mapOfNodes: Sequence<Trace>,
    features: MutableList<Feature>,
    displayRoute: Route?,
) {
    val timeGeneration = measureTime {
        mapOfNodes
            .map { toFeaturesFrom(it) }
            .toCollection(features)
            .also { list ->
                displayRoute?.let {
                    list.add(toLineStringFeature(displayRoute))
                }
            }
    }
    Log.d(
        "RENDER_DEBUG",
        "Feature collection time generation - " +
                "${timeGeneration.inWholeMilliseconds} ms for [${features.size}] features. "
    )
}


private fun toLineStringFeature(displayRoute: Route) = Feature.fromGeometry(
    LineString.fromLngLats(displayRoute.coordinates.map {
        Point.fromLngLat(it.lon, it.lat)
    })
)

private fun toFeaturesFrom(entry: Trace) =
    Feature.fromGeometry(
        Point.fromLngLat(
            entry.lon,
            entry.lat
        ), null, entry.nodeId
    )
        .apply {
            addStringProperty(
                TEXT_FIELD_KEY_PROPERTY,
                String.format(
                    "\n%s: %d°\n%d m/s",
                    entry.direction,
                    entry.azimuth.toInt(),
                    entry.speed
                )
            )
            addStringProperty(
                MODE_KEY_PROPERTY,
                io.architecture.model.NodeMode.ACTIVE.name
            ) // TODO Not available from Trace class
            addStringProperty(NODE_ID_KEY_PROPERTY, entry.nodeId)
            addNumberProperty(BEARING_KEY_PROPERTY, entry.azimuth)
        }
