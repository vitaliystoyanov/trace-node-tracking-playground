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
import io.architecture.playground.model.Node
import io.architecture.playground.model.Route
import io.architecture.playground.model.Trace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val nodesState by viewModel.nodesUiState.collectAsStateWithLifecycle()
    val connectionState by viewModel.connectionState.collectAsStateWithLifecycle()

    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedNodeId by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState()

    Scaffold { contentPadding ->
        MapNodesContent(
            contentPadding,
            nodesState.nodeTraceMap,
            state.displayRoute,
            onNodeClick = { nodeId ->
                selectedNodeId = nodeId
                viewModel.loadRoute(nodeId)
                showBottomSheet = true
            },
            onReleaseObjectFromPool = {
                viewModel.releaseRenderedObjects()
            }
        )
        StatusBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp),
            connectionState.all,
            nodesState.nodeCount
        )

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    selectedNodeId = ""
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                val node =
                    nodesState.nodeTraceMap.entries.find { it.key.id == selectedNodeId } // TODO UFD
                node?.let {
                    NodeBottomSheetContent(node, selectedNodeId)
                }
            }
        }
    }
}

@OptIn(MapboxExperimental::class)
@Composable
fun MapNodesContent(
    padding: PaddingValues,
    mapOfNodes: Map<Node, Trace>,
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
        compassSettings = CompassSettings { enabled = true; rotation = 0f; clickable = true },
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

        MapEffect(mapOfNodes) { view ->
            val source = view.mapboxMap.getSource(NODE_DATA_SOURCE_ID) as? GeoJsonSource
            val features = mutableListOf<Feature>()


            val timeGeneration = measureTime {
                mapOfNodes.entries
                    .asSequence()
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


private fun toLineStringFeature(displayRoute: Route) = Feature.fromGeometry(
    LineString.fromLngLats(displayRoute.coordinates.map {
        Point.fromLngLat(it.lon, it.lat)
    })
)

private fun toFeaturesFrom(entry: Map.Entry<Node, Trace>) =
    Feature.fromGeometry(
        Point.fromLngLat(
            entry.value.lon,
            entry.value.lat
        ), null, entry.value.nodeId
    )
        .apply {
            addStringProperty(
                TEXT_FIELD_KEY_PROPERTY,
                String.format(
                    "\n%s: %d°\n%d m/s",
                    entry.value.direction,
                    entry.value.azimuth.toInt(),
                    entry.value.speed
                )
            )
            addStringProperty(MODE_KEY_PROPERTY, entry.key.mode.toString())
            addStringProperty(NODE_ID_KEY_PROPERTY, entry.value.nodeId)
            addNumberProperty(BEARING_KEY_PROPERTY, entry.value.azimuth)
        }
