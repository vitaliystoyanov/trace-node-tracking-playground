package io.architecture.playground.feature.map

import android.graphics.Color
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.style.expressions.dsl.generated.get
import com.mapbox.maps.extension.style.expressions.dsl.generated.match
import com.mapbox.maps.extension.style.expressions.generated.Expression.Companion.eq
import com.mapbox.maps.extension.style.expressions.generated.Expression.Companion.interpolate
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.circleLayer
import com.mapbox.maps.extension.style.layers.generated.lineLayer
import com.mapbox.maps.extension.style.layers.generated.symbolLayer
import com.mapbox.maps.extension.style.layers.properties.generated.TextAnchor
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.mapbox.maps.extension.style.sources.getSource
import com.mapbox.maps.plugin.attribution.generated.AttributionSettings
import com.mapbox.maps.plugin.compass.generated.CompassSettings
import com.mapbox.maps.plugin.scalebar.generated.ScaleBarSettings
import io.architecture.playground.feature.map.MapBoxParams.CIRCLE_RADIUS
import io.architecture.playground.feature.map.MapBoxParams.LAYER_CIRCLE_ID
import io.architecture.playground.feature.map.MapBoxParams.LAYER_LINE_ID
import io.architecture.playground.feature.map.MapBoxParams.LAYER_TEXT_ID
import io.architecture.playground.feature.map.MapBoxParams.LINE_WIDTH
import io.architecture.playground.feature.map.MapBoxParams.PITCH
import io.architecture.playground.feature.map.MapBoxParams.SOURCE_ID
import io.architecture.playground.feature.map.MapBoxParams.ZOOM

object MapBoxParams {
    const val ZOOM = 4.5
    const val PITCH = 0.0
    const val CIRCLE_RADIUS = 2.8
    const val LINE_WIDTH = 2.0
    const val SOURCE_ID = "source-id"
    const val LAYER_CIRCLE_ID = "layer-circle-id"
    const val LAYER_LINE_ID = "layer-line-id"
    const val LAYER_TEXT_ID = "layer-text-id"
}

@OptIn(MapboxExperimental::class)
@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel(),
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            center(Point.fromLngLat(34.0828899, 44.1541579))
            zoom(ZOOM)
            pitch(PITCH)
        }
    }

    val compassSettings: CompassSettings by remember {
        mutableStateOf(CompassSettings { enabled = false })
    }

    val scaleBarSetting: ScaleBarSettings by remember {
        mutableStateOf(ScaleBarSettings { enabled = false })
    }

    Box(modifier = Modifier.fillMaxSize()) {
        MapboxMap(
            Modifier.fillMaxSize(),
            mapInitOptionsFactory = { context ->
                MapInitOptions(
                    context = context
                )
            },
            mapViewportState = mapViewportState,
            compassSettings = compassSettings,
            scaleBarSettings = scaleBarSetting,
            attributionSettings = AttributionSettings {
                enabled = false
            },
        ) {
            // TODO Migrate to MapBox Style Extension
            MapEffect(Unit) {
                it.mapboxMap.getStyle {
                    it.addSource(
                        geoJsonSource(SOURCE_ID) {
                            featureCollection(
                                FeatureCollection.fromFeature(
                                    Feature.fromGeometry(Point.fromLngLat(0.0, 0.0))
                                )
                            )
                        }
                    )
                    it.addLayer(
                        circleLayer(LAYER_CIRCLE_ID, SOURCE_ID) {
                            circleColor(match {
                                get("mode")
                                stop {
                                    literal(1) // moving
                                    color(Color.GREEN)
                                }
                                stop {
                                    literal(0) // not moving
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
                    it.addLayer(
                        lineLayer(LAYER_LINE_ID, SOURCE_ID) {
                            lineColor(Color.BLACK)
                            lineWidth(LINE_WIDTH)
                            filter(
                                eq {
                                    literal("\$type")
                                    literal("LineString")
                                }
                            )
                        }
                    )
                    it.addLayer(
                        symbolLayer(LAYER_TEXT_ID, SOURCE_ID) {
                            textField(get { literal("text-field") })
                            textAnchor(TextAnchor.TOP_RIGHT)
                            textPadding(5.0)
                            textOptional(true)
                            textColor(Color.BLACK)
                            textEmissiveStrength(4.0)
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
                                        literal(10)
                                        literal(7.5)
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
            }

            MapEffect(state.value) { view ->
                view.mapboxMap.getStyle {

                    val source = view.mapboxMap.getSource(SOURCE_ID) as? GeoJsonSource
                    source?.featureCollection(
                        FeatureCollection.fromFeatures(
                            state.value.latestTraces.map {
                                Feature.fromGeometry(
                                    Point.fromLngLat(
                                        it.lon,
                                        it.lat
                                    )
                                ).also { feature ->
                                    feature.addStringProperty(
                                        "text-field",
                                        String.format("%d m/s\n %.2f", it.speed, it.bearing)
                                    )
                                    feature.addNumberProperty("mode", it.mode)
                                }
                            }.toMutableList()
                                .also {
                                    state.value.latestTraceRoutes.forEach { (_, value) ->
                                        run {
                                            val lineString: LineString =
                                                LineString.fromLngLats(value.map {
                                                    Point.fromLngLat(
                                                        it.lon,
                                                        it.lat
                                                    )
                                                })
                                            it.add(Feature.fromGeometry(lineString))
                                        }
                                    }
                                }
                        )
                    )
                }
            }

//            LaunchedEffect(state.value) {
//                mapViewportState.flyTo(
//                    cameraOptions = cameraOptions {
//                        center(Point.fromLngLat(state.value.trace.lon, state.value.trace.lat))
////                        zoom(ZOOM)
//                    },
//                    animationOptions = MapAnimationOptions.mapAnimationOptions { duration(100) },
//                )
//            }
        }



        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = "Connection: ${state.value.connection.type.name}",
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Collected trace: ${state.value.tracesCount}",
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Track nodes: ${state.value.latestTraces.size}",
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}