import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
}

kotlin {

    jvm("desktop")

    sourceSets {
        val desktopMain by getting {
            dependencies {
                implementation(projects.core.di)
                implementation(projects.core.datasource.api)
                implementation(projects.feature.map)
                // from Composite Gradle Build. See root setting.gradle.kts
                //noinspection UseTomlInstead
                implementation("ca.derekellis.mapbox:compose-mapbox-library") // TODO For testing
                implementation(libs.koin.core)
                implementation(libs.koin.mp.compose)
                implementation(compose.desktop.currentOs)
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "io.architecture.playground"
            packageVersion = "1.0.0"
        }
    }
}