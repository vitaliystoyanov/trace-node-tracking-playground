package io.architecture.core.design.system.theme

import androidx.compose.material.darkColors
import androidx.compose.ui.graphics.Color

val purple_500 = Color(0xFF6200EE)
val purple_700 = Color(0xFF3700B3)
val orange_700 = Color(0xFFA00000)
val teal_200 = Color(0xFF03DAC5)
val teal_700 = Color(0xFF018786)
val teal_700_dark = Color(0xFF006463)
val teal_red = Color(0xFFD32F2F)
val black = Color(0xFF000000)
val white = Color(0xFFFFFFFF)

val colorPallete =
    darkColors(
        primary = purple_500,
        primaryVariant = purple_700,
        secondary = teal_700,
        secondaryVariant = teal_700_dark,
        background = black,
        surface = black,
        onPrimary = black,
        onSecondary = black,
        onBackground = black,
        onSurface = black,
        error = teal_red,
        onError = teal_red
    )

val yellow_green = Color(0xFFA2F854)
val yellow_green_secondary = Color(0xFF6EF633)
val green_secondary = Color(0xFF02BC87)
val marquee_background = Color(0xFF62E788)

val green_state_success = Color(0xFF2ED573)
val yellow_state_warning = Color(0xFFFFBE21)
val red_state_danger = Color(0xFFEA5B5B)