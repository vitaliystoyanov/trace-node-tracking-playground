package io.architecture.compose.web.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import ca.derekellis.mapbox.MapboxMap
import ca.derekellis.mapbox.rememberMapboxState
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.css.vw
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposable

external fun require(module: String): dynamic

fun main() {
    require("mapbox-gl/dist/mapbox-gl.css")

    renderComposable(rootElementId = "root") {
        val mapState = rememberMapboxState()
        var isDarkTheme by remember { mutableStateOf(true) }

        if (isDarkTheme) Style(Dark) else Style(Light)
        Style(MainStyleSheet)

        MapboxMap(
            accessToken = BuildKonfig.MAPBOX_ACCESS_TOKEN,
            style = if (isDarkTheme) "mapbox://styles/mapbox/dark-v10" else "mapbox://styles/mapbox/light-v10",
            state = mapState,
            containerAttrs = {
                style {
                    height(100.vh)
                    width(100.vw)
                }
            },
        ) {
            // TODO GeoJson source...
        }
        Div(attrs = { classes(MainStyleSheet.overlay) }) {
            Div {
                Label(forId = "theme-toggle") { Text("Toggle Theme:") }
                Button(attrs = {
                    id("theme-toggle")
                    onClick { isDarkTheme = !isDarkTheme }
                }) { Text(if (isDarkTheme) "Dark" else "Light") }
            }
        }
    }
}