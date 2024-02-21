import dev.iurysouza.modulegraph.LinkText
import dev.iurysouza.modulegraph.Orientation
import dev.iurysouza.modulegraph.Theme

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.androidx.benchmark) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.modulegraph)
}

moduleGraphConfig {
    readmePath.set("./README.md")
    heading.set("## Module Dependency Diagram")
    showFullPath.set(true)
    orientation.set(Orientation.LEFT_TO_RIGHT)
    linkText.set(LinkText.NONE)
    theme.set(
        Theme.BASE(
            mapOf(
                "primaryTextColor" to "#fff",
                "primaryColor" to "#6A00FF",
                "primaryBorderColor" to "#6A00FF",
                "lineColor" to "#f5a623",
                "tertiaryColor" to "#40375c",
                "fontSize" to "11px"
            )
        )
    )
}