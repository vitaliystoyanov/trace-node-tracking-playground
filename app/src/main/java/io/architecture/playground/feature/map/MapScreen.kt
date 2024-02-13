package io.architecture.playground.feature.map

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.mapbox.maps.renderer.OnFpsChangedListener
import io.architecture.playground.feature.map.MapBoxParams.CAMERA_POINT
import io.architecture.playground.feature.map.MapBoxParams.PITCH
import io.architecture.playground.feature.map.MapBoxParams.ZOOM
import io.architecture.playground.model.Route
import io.architecture.playground.model.Trace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.measureTime


object MapBoxParams {
    const val ZOOM = 4.5
    const val PITCH = 0.0
    val CAMERA_POINT: Point = Point.fromLngLat(34.0828899, 44.1541579)
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel(),
) {
    val details by viewModel.detailsUiState.collectAsStateWithLifecycle()
    val traces by viewModel.tracesUiState.collectAsStateWithLifecycle()
    val nodesCounter by viewModel.nodeCounterUiState.collectAsStateWithLifecycle()
    val connectionState by viewModel.connectionsUiState.collectAsStateWithLifecycle()

    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedNodeId by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState()

    Scaffold { contentPadding ->
        MapNodesContent(
            contentPadding,
            traces,
            details.route,
            onNodeClick = { nodeId ->
                selectedNodeId = nodeId
                viewModel.loadDetails(nodeId)
                showBottomSheet = true
            }
        ) {

        }
        StatusBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp),
            connectionState,
            nodesCounter
        )

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    selectedNodeId = ""
                    showBottomSheet = false
                    viewModel.clearDetails(selectedNodeId)
                },
                sheetState = sheetState
            ) {
                NodeBottomSheetContent(details, selectedNodeId)
            }
        }
    }
}

@OptIn(MapboxExperimental::class)
@Composable
fun MapNodesContent(
    padding: PaddingValues,
    nodeTraces: Sequence<Trace>,
    displayRoute: Route?,
    onNodeClick: (String) -> Unit,
    onReleaseObjectFromPool: () -> Unit
) {
    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            center(CAMERA_POINT)
            zoom(ZOOM)
            pitch(PITCH)
        }
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
        compassSettings = CompassSettings { enabled = false; rotation = 0f; clickable = true },
        scaleBarSettings = scaleBarSetting,
        gesturesSettings = gesturesSettings,
    ) {
        MapEffect(Unit) { view ->
            with(view.mapboxMap) {
                loadStyle(createStyle(view.context))
                addOnMapClickListener { point ->
                    queryRenderedFeatures(
                        RenderedQueryGeometry(pixelForCoordinate(point)),
                        RenderedQueryOptions(listOf(LAYER_SYMBOL_ID, LAYER_CIRCLE_ID), null)
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
                OnFpsChangedListener { fps ->
                    Log.d("RENDER_DEBUG", "Changed FPS: $fps")
                }
            }
        }

        MapEffect(nodeTraces) { view ->
            val source = view.mapboxMap.getSource(NODE_DATA_SOURCE_ID) as? GeoJsonSource
            val features = mutableListOf<Feature>()

            withContext(Dispatchers.Default) {
                generateFeatures(nodeTraces, features, displayRoute)
            }
            val timeAddToFeatureCollection = measureTime {
                source?.featureCollection(FeatureCollection.fromFeatures(features))
                onReleaseObjectFromPool()
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
    displayRoute: Route?
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
            addStringProperty(MODE_KEY_PROPERTY, "entry.mode.toString()")
            addStringProperty(NODE_ID_KEY_PROPERTY, entry.nodeId)
            addNumberProperty(BEARING_KEY_PROPERTY, entry.azimuth)
        }
