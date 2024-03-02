package io.architecture.compose.web.app

import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.fontFamily
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.left
import org.jetbrains.compose.web.css.margin
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.position
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.top
import org.jetbrains.compose.web.css.value
import org.jetbrains.compose.web.css.variable
import org.jetbrains.compose.web.css.width

object MainStyle {
    val textPrimary by variable<CSSColorValue>()
    val backgroundPrimary by variable<CSSColorValue>()
}

object MainStyleSheet : StyleSheet() {
    init {
        "html, body, #root" style {
            width(100.percent)
            height(100.percent)
            margin(0.px)
            fontFamily("sans-serif")
        }

        "body" {
            backgroundColor(MainStyle.backgroundPrimary.value())
        }
    }

    val overlay by style {
        position(Position.Fixed)
        top(0.px)
        left(0.px)
        color(MainStyle.textPrimary.value())
    }
}