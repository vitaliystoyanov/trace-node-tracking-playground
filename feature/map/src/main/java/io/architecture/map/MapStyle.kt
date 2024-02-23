package io.architecture.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.expressions.dsl.generated.eq
import com.mapbox.maps.extension.style.expressions.dsl.generated.get
import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.extension.style.expressions.dsl.generated.match
import com.mapbox.maps.extension.style.image.image
import com.mapbox.maps.extension.style.layers.generated.circleLayer
import com.mapbox.maps.extension.style.layers.generated.lineLayer
import com.mapbox.maps.extension.style.layers.generated.symbolLayer
import com.mapbox.maps.extension.style.layers.properties.generated.LineCap
import com.mapbox.maps.extension.style.layers.properties.generated.LineJoin
import com.mapbox.maps.extension.style.layers.properties.generated.TextAnchor
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.mapbox.maps.extension.style.sources.generated.vectorSource
import com.mapbox.maps.extension.style.style


private const val CIRCLE_RADIUS = 2.8
private const val LINE_WIDTH = 2.0

// GEOJson data sources' ids
private const val TERRAIN_DATA_SOURCE_ID = "terrain-data-source-id"

//    Json layers' and image ids
const val NODE_DATA_SOURCE_ID = "source-id"
const val LAYER_CIRCLE_ID = "layer-circle-id"
const val LAYER_LINE_ID = "layer-line-id"
const val LAYER_SYMBOL_ID = "symbol-text-id"
private const val TRIANGLE_IMAGE_ID = "triangle-image-id"
private const val TERRAIN_SOURCE_LAYER_ID = "contour"

//   Json Feature properties
const val TEXT_FIELD_KEY_PROPERTY = "text-field"
const val NODE_ID_KEY_PROPERTY = "node-id"
const val MODE_KEY_PROPERTY = "mode"
const val BEARING_KEY_PROPERTY = "bearing"

private const val TERRAIN_DATA_MAPBOX_URL = "mapbox://mapbox.mapbox-terrain-v2"

fun createStyle(context: Context) = style(style = Style.LIGHT) {
    +vectorSource(TERRAIN_DATA_SOURCE_ID) {
        url(TERRAIN_DATA_MAPBOX_URL)
    }
    +geoJsonSource(NODE_DATA_SOURCE_ID) {
        buffer(0)
    }

    +lineLayer(TERRAIN_DATA_SOURCE_ID, TERRAIN_DATA_SOURCE_ID) {
        sourceLayer(TERRAIN_SOURCE_LAYER_ID)
        lineJoin(LineJoin.ROUND)
        lineCap(LineCap.ROUND)
        lineColor(Color.parseColor("#8C3C64"))
        lineWidth(1.9)
    }
    bitmapFromDrawableRes(context, R.drawable.triangle)?.let {
        +image(TRIANGLE_IMAGE_ID, it)
    }
    +circleLayer(LAYER_CIRCLE_ID, NODE_DATA_SOURCE_ID) {
        circleColor(match {
            get(MODE_KEY_PROPERTY)
            stop {
                literal(io.architecture.model.NodeMode.ACTIVE.toString())
                color(Color.GREEN)
            }
            stop {
                literal(io.architecture.model.NodeMode.INACTIVE.toString())
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
                    literal(0.5)
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
    +lineLayer(LAYER_LINE_ID, NODE_DATA_SOURCE_ID) {
        lineColor(Color.parseColor("#ff69b4"))
        lineWidth(LINE_WIDTH)
        filter(
            eq {
                literal("\$type")
                literal("LineString")
            }
        )
    }
    +symbolLayer(LAYER_SYMBOL_ID, NODE_DATA_SOURCE_ID) {
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
        iconOpacity(
            interpolate {
                linear()
                zoom()
                stop {
                    literal(10)
                    literal(0)
                }
                stop {
                    literal(20)
                    literal(100)
                }
            }
        )
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
        textOpacity(
            interpolate {
                linear()
                zoom()
                stop {
                    literal(7.5)
                    literal(0)
                }
                stop {
                    literal(11)
                    literal(100)
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
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
private fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int) =
    drawableToBitmap(AppCompatResources.getDrawable(context, resourceId))

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
private fun drawableToBitmap(
    sourceDrawable: Drawable?,
    flipX: Boolean = false,
    flipY: Boolean = false,
    @ColorInt tint: Int? = null,
): Bitmap? {
    if (sourceDrawable == null) {
        return null
    }
    return if (sourceDrawable is BitmapDrawable) {
        sourceDrawable.bitmap
    } else {
        // copying drawable object to not manipulate on the same reference
        val constantState = sourceDrawable.constantState ?: return null
        val drawable = constantState.newDrawable().mutate()
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth, drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        tint?.let(drawable::setTint)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        canvas.scale(
            if (flipX) -1f else 1f,
            if (flipY) -1f else 1f,
            canvas.width / 2f,
            canvas.height / 2f
        )
        drawable.draw(canvas)
        bitmap
    }
}