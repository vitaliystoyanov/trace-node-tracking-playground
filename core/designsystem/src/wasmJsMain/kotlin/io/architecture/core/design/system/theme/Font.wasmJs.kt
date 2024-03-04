package io.architecture.core.design.system.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.FontResource

@OptIn(ExperimentalResourceApi::class)
@Composable
actual fun font(
    name: String,
    res: String,
    weight: FontWeight,
    style: FontStyle,
): Font = Font(FontResource("fonts/$res.ttf"))