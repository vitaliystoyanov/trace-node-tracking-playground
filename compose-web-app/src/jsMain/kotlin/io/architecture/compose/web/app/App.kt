package io.architecture.compose.web.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.architecture.core.di.coreKoinModules
import io.architecture.datasource.api.emptyMockLocalDatasourceModule
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposable
import org.koin.compose.KoinApplication

fun main() {
    renderComposable(rootElementId = "root") {
        var isDarkTheme by remember { mutableStateOf(true) }

        if (isDarkTheme) Style(Dark) else Style(Light)
        Style(MainStyleSheet)

        KoinApplication(application = {
            modules(coreKoinModules, emptyMockLocalDatasourceModule)
        }) {
            // TODO Compose HTML
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