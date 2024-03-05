package io.architecture.core.design.system.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import kotlinx.coroutines.runBlocking
import node_traces_streaming.core.designsystem.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

private val cache: MutableMap<String, Font> = mutableMapOf()

@OptIn(ExperimentalResourceApi::class)
@Composable
actual fun font(
    name: String,
    res: String,
    weight: FontWeight,
    style: FontStyle,
): Font = cache.getOrPut(res) {
    Font(
        res,
        runBlocking {
            Res.readBytes("font/$res.ttf")
        }, weight, style
    )
}