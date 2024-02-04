package io.architecture.playground.feature.map

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.style.expressions.dsl.generated.get
import com.mapbox.maps.extension.style.expressions.dsl.generated.match
import com.mapbox.maps.extension.style.expressions.generated.Expression.Companion.eq
import com.mapbox.maps.extension.style.expressions.generated.Expression.Companion.interpolate
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.addLayerBelow
import com.mapbox.maps.extension.style.layers.generated.circleLayer
import com.mapbox.maps.extension.style.layers.generated.lineLayer
import com.mapbox.maps.extension.style.layers.generated.symbolLayer
import com.mapbox.maps.extension.style.layers.properties.generated.LineCap
import com.mapbox.maps.extension.style.layers.properties.generated.LineJoin
import com.mapbox.maps.extension.style.layers.properties.generated.TextAnchor
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.mapbox.maps.extension.style.sources.generated.vectorSource
import com.mapbox.maps.extension.style.sources.getSource
import com.mapbox.maps.plugin.compass.generated.CompassSettings
import com.mapbox.maps.plugin.gestures.OnMapClickListener
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.gestures.generated.GesturesSettings
import com.mapbox.maps.plugin.scalebar.generated.ScaleBarSettings
import io.architecture.playground.R
import io.architecture.playground.data.remote.model.SocketConnectionState
import io.architecture.playground.feature.map.MapBoxParams.BEARING_KEY_PROPERTY
import io.architecture.playground.feature.map.MapBoxParams.CIRCLE_RADIUS
import io.architecture.playground.feature.map.MapBoxParams.LAYER_CIRCLE_ID
import io.architecture.playground.feature.map.MapBoxParams.LAYER_LINE_ID
import io.architecture.playground.feature.map.MapBoxParams.LAYER_SYMBOL_ID
import io.architecture.playground.feature.map.MapBoxParams.LINE_WIDTH
import io.architecture.playground.feature.map.MapBoxParams.MODE_KEY_PROPERTY
import io.architecture.playground.feature.map.MapBoxParams.NODE_ID_KEY_PROPERTY
import io.architecture.playground.feature.map.MapBoxParams.PITCH
import io.architecture.playground.feature.map.MapBoxParams.SOURCE_ID
import io.architecture.playground.feature.map.MapBoxParams.TERRAIN_DATA_SOURCE_ID
import io.architecture.playground.feature.map.MapBoxParams.TERRAIN_SOURCE_LAYER_ID
import io.architecture.playground.feature.map.MapBoxParams.TEXT_FIELD_KEY_PROPERTY
import io.architecture.playground.feature.map.MapBoxParams.TRIANGLE_IMAGE_ID
import io.architecture.playground.feature.map.MapBoxParams.ZOOM
import io.architecture.playground.model.Node
import io.architecture.playground.model.NodeMode
import io.architecture.playground.util.BitmapUtils.bitmapFromDrawableRes
import io.architecture.playground.util.azimuthToDirection
import io.architecture.playground.util.toDatetime

object MapBoxParams {
    const val ZOOM = 4.5
    const val PITCH = 0.0
    const val CIRCLE_RADIUS = 2.8
    const val LINE_WIDTH = 2.0

    // GEOJson data sources' ids
    const val TERRAIN_DATA_SOURCE_ID = "terrain-data-source-id"

    // GEOJson layers' and image ids
    const val SOURCE_ID = "source-id"
    const val LAYER_CIRCLE_ID = "layer-circle-id"
    const val LAYER_LINE_ID = "layer-line-id"
    const val LAYER_SYMBOL_ID = "symbol-text-id"
    const val TRIANGLE_IMAGE_ID = "triangle-image-id"
    const val TERRAIN_SOURCE_LAYER_ID = "contour"

    // GEOJson Feature properties
    const val TEXT_FIELD_KEY_PROPERTY = "text-field"
    const val NODE_ID_KEY_PROPERTY = "node-id"
    const val MODE_KEY_PROPERTY = "mode"
    const val BEARING_KEY_PROPERTY = "bearing"
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel(),
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedNode by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState()

    Scaffold { contentPadding ->
        MapNodesContent(contentPadding,
            state,
            selectedNode,
            onNodeClick = { nodeId ->
                selectedNode = nodeId
                showBottomSheet = true
            }
        )
        TopConnectionStatusBar(state)
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                selectedNode = ""
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            BottomSheetContent(state, selectedNode)
        }
    }
}

@Composable
fun BottomSheetContent(state: State<MapNodesUiState>, selectedNode: String) {
    val selectedNodeTracesList = state.value.latestNodeTraces[selectedNode]
    val lastNode = selectedNodeTracesList?.last()
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        listOf(
            buildAnnotatedString {
                append(
                    String.format(
                        "%s: %f° (reference plane is true north)",
                        lastNode?.let { azimuthToDirection(it.azimuth) },
                        lastNode?.azimuth,
                    )
                )
            },
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Attitude: ")
                }
                append(lastNode?.alt.toString())
            },
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Speed: ")
                }
                append("${lastNode?.speed} m/s")
            },
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Longitude: ")
                }
                append(lastNode?.lon.toString())
            },
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Latitude: ")
                }
                append(lastNode?.lat.toString())
            },
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Mode: ")
                }
                append(lastNode?.mode.toString())
            },
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("All collected traces: ")
                }
                append(selectedNodeTracesList?.size.toString())
            },
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Last traced timestamp: ")
                }
                append(lastNode?.time?.let { toDatetime(it).toString() })
            },
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Node ID: ")
                }
                append(selectedNode)
            }
        ).forEach { text ->
            Text(
                text = text,
                fontSize = 13.sp
            )
        }
    }
}

@OptIn(MapboxExperimental::class)
@Composable
fun MapNodesContent(
    padding: PaddingValues,
    state: State<MapNodesUiState>,
    selectedNode: String,
    onNodeClick: (String) -> Unit
) {
    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            center(Point.fromLngLat(34.0828899, 44.1541579))
            zoom(ZOOM)
            pitch(PITCH)
        }
    }
    val compassSettings: CompassSettings by remember {
        mutableStateOf(CompassSettings { enabled = true })
    }
    val scaleBarSetting: ScaleBarSettings by remember {
        mutableStateOf(ScaleBarSettings { enabled = false })
    }
    val gesturesSettings: GesturesSettings by remember {
        mutableStateOf(GesturesSettings { rotateEnabled = false })
    }

    MapboxMap(
        Modifier.padding(padding),
        mapInitOptionsFactory = { context ->
            MapInitOptions(
                context = context,
                styleUri = Style.LIGHT
            )
        },
        mapViewportState = mapViewportState,
        compassSettings = compassSettings,
        scaleBarSettings = scaleBarSetting,
        gesturesSettings = gesturesSettings,
    ) {
        // TODO Migrate to MapBox Style Extension
        MapEffect(Unit) { mapView ->
            mapView.mapboxMap.getStyle { style ->
                style.addSource(
                    vectorSource(TERRAIN_DATA_SOURCE_ID) {
                        url("mapbox://mapbox.mapbox-terrain-v2")
                    })
                style.addSource(
                    geoJsonSource(SOURCE_ID)
                )
                style.addLayer(
                    lineLayer(TERRAIN_DATA_SOURCE_ID, TERRAIN_DATA_SOURCE_ID) {
                        sourceLayer(TERRAIN_SOURCE_LAYER_ID)
                        lineJoin(LineJoin.ROUND)
                        lineCap(LineCap.ROUND)
                        lineColor(Color.parseColor("#ff69b4"))
                        lineWidth(1.9)
                    }
                )
                bitmapFromDrawableRes(mapView.context, R.drawable.triangle)?.let { bitmap ->
                    style.addImage(TRIANGLE_IMAGE_ID, bitmap)
                }
                style.addLayer(
                    circleLayer(LAYER_CIRCLE_ID, SOURCE_ID) {
                        circleColor(match {
                            get(MODE_KEY_PROPERTY)
                            stop {
                                literal(NodeMode.ACTIVE.toString())
                                color(Color.GREEN)
                            }
                            stop {
                                literal(NodeMode.INACTIVE.toString())
                                color(Color.RED)
                            }
                            color(Color.BLACK)
                        })
                        circleStrokeColor(Color.BLACK)
                        circleStrokeWidth(1.0)
                        circleRadius(
                            interpolate {
                                exponential {
                                    literal(1.75)
                                }
                                zoom()
                                stop {
                                    literal(4.5)
                                    literal(1)
                                }
                                stop {
                                    literal(10)
                                    literal(CIRCLE_RADIUS)
                                }
                            }
                        )
                        filter(
                            eq {
                                literal("\$type")
                                literal("Point")
                            }
                        )
                    }
                )
                style.addLayerBelow(
                    lineLayer(LAYER_LINE_ID, SOURCE_ID) {
                        lineColor(Color.BLACK)
                        lineWidth(LINE_WIDTH)
                        filter(
                            eq {
                                literal("\$type")
                                literal("LineString")
                            }
                        )
                    }, below = LAYER_CIRCLE_ID
                )
                style.addLayer(
                    symbolLayer(LAYER_SYMBOL_ID, SOURCE_ID) {
                        iconImage(TRIANGLE_IMAGE_ID)
                        iconIgnorePlacement(true)
                        iconAllowOverlap(true)
                        iconOffset(listOf(0.0, -2.0))
                        iconSize(
                            interpolate {
                                exponential {
                                    literal(1.2)
                                }
                                zoom()
                                stop {
                                    literal(4.5)
                                    literal(1)
                                }
                                stop {
                                    literal(10)
                                    literal(3)
                                }
                            }
                        )
                        iconRotate(get(BEARING_KEY_PROPERTY))
                        textField(get { literal(TEXT_FIELD_KEY_PROPERTY) })
                        textAnchor(TextAnchor.TOP_RIGHT)
                        textPadding(5.0)
                        textOptional(true)
                        textColor(Color.BLACK)
                        textEmissiveStrength(10.0)
                        textSize(
                            interpolate {
                                exponential {
                                    literal(1.75)
                                }
                                zoom()
                                stop {
                                    literal(4.5)
                                    literal(2)
                                }
                                stop {
                                    literal(9)
                                    literal(9.0)
                                }
                            }
                        )
                        textAllowOverlap(true)
                        filter(
                            eq {
                                literal("\$type")
                                literal("Point")
                            }
                        )
                    }
                )
            }
            mapView.mapboxMap.addOnMapClickListener(OnMapClickListener { point ->
                mapView.mapboxMap.queryRenderedFeatures(
                    RenderedQueryGeometry(mapView.mapboxMap.pixelForCoordinate(point)),
                    RenderedQueryOptions(listOf(LAYER_SYMBOL_ID, LAYER_CIRCLE_ID), null)
                ) {
                    it.value?.forEach { q ->
                        onNodeClick(q.queriedFeature.feature.getStringProperty(NODE_ID_KEY_PROPERTY))
                    }
                }
                true
            })
        }

        MapEffect(state.value) { view ->
            val source = view.mapboxMap.getSource(SOURCE_ID) as? GeoJsonSource
            source?.featureCollection(
                FeatureCollection.fromFeatures(
                    state.value.latestNodes.map {
                        Feature.fromGeometry(
                            Point.fromLngLat(
                                it.lon,
                                it.lat
                            )
                        ).also { feature ->
                            feature.addStringProperty(
                                TEXT_FIELD_KEY_PROPERTY,
                                String.format(
                                    "\n%s: %d°\n%d m/s",
                                    azimuthToDirection(it.azimuth),
                                    it.azimuth.toInt(),
                                    it.speed
                                )
                            )
                            feature.addStringProperty(MODE_KEY_PROPERTY, it.mode.name)
                            feature.addStringProperty(NODE_ID_KEY_PROPERTY, it.nodeId)
                            feature.addNumberProperty(BEARING_KEY_PROPERTY, it.azimuth)
                        }
                    }.toMutableList()
                        .also { listFeatures ->
                            if (selectedNode.isNotEmpty()) {
                                state.value.latestNodeTraces[selectedNode]?.let { traces ->
                                    generateLineStringFeaturesBy(traces, listFeatures)
                                }
                            }
                        }
                )
            )
        }
    }
}

private fun generateLineStringFeaturesBy(
    value: List<Node>,
    destinationList: MutableList<Feature>
) {
    run {
        val lineString: LineString =
            LineString.fromLngLats(value.map {
                Point.fromLngLat(
                    it.lon,
                    it.lat
                )
            })
        destinationList.add(Feature.fromGeometry(lineString))
    }
}

@Composable
fun TopConnectionStatusBar(state: State<MapNodesUiState>) {
    val bgColor = when (state.value.connectionState.type) {
        SocketConnectionState.UNDEFINED -> R.color.black
        SocketConnectionState.OPENED -> R.color.teal_700
        SocketConnectionState.CLOSED -> R.color.teal_red
        SocketConnectionState.CLOSING -> R.color.teal_700
        SocketConnectionState.FAILED -> R.color.teal_red
        SocketConnectionState.MESSAGE_RECEIVED -> R.color.teal_700
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
            .background(colorResource(bgColor)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        with(state.value) {
            if (this.connectionState.type === SocketConnectionState.OPENED) { // Dirty, very dirty
                listOf(
                    "Track nodes: ${this.nodeCount}"
                ).forEach { text ->
                    Text(
                        text = text,
                        color = colorResource(id = R.color.white),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else if (this.connectionState.type === SocketConnectionState.UNDEFINED) {
                Text(
                    text = "Connecting...",
                    color = colorResource(id = R.color.white),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Text(
                    text = "Connection status: ${
                        this.connectionState.type.toString().lowercase()
                    }!",
                    color = colorResource(id = R.color.white),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}