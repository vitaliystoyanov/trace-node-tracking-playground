package io.architecture.core.design.system.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun UAVTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = colorPallete,
        content = content,
        shapes = shapes,
        typography = getTypography()
    )
}