package io.architecture.core.design.system.theme

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun getTypography(): Typography {
    FontFamily(
        font(
            name = "UAV OSD Mono",
            res = "uav_osd_mono",
            weight = FontWeight.Normal,
            style = FontStyle.Normal
        )
    )
    FontFamily(
        font(
            name = "UAV OSD Sans Mono",
            res = "vcr_osd_mono",
            weight = FontWeight.Normal,
            style = FontStyle.Normal
        )
    )

    val default = FontFamily(
        font(
            name = "UAV OSD Sans Mono",
            res = "uav_osd_sans_mono",
            weight = FontWeight.Normal,
            style = FontStyle.Normal
        )
    )

    return Typography(
        defaultFontFamily = default,
        h1 = TextStyle(
            fontSize = 38.sp,
            fontWeight = FontWeight.Bold,
        ),
        h2 = TextStyle(
            fontWeight = FontWeight.Light,
            fontSize = 60.sp,
            lineHeight = 72.sp,
            letterSpacing = (-0.5).sp
        ),
        h3 = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 48.sp,
            lineHeight = 56.sp,
            letterSpacing = 0.sp
        ),
        h4 = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 34.sp,
            lineHeight = 36.sp,
            letterSpacing = 0.25.sp
        ),
        h5 = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.sp
        ),
        h6 = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.15.sp
        ),
        subtitle1 = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.15.sp
        ),
        subtitle2 = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.1.sp
        ),
        body1 = TextStyle(
            fontSize = 12.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.Normal,
        ),
        body2 = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.25.sp
        ),
        button = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp,
        ),
        caption = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.4.sp
        ),
        overline = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 10.sp,
            lineHeight = 16.sp,
            letterSpacing = 1.5.sp
        )
    )
}