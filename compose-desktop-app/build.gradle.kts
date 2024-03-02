import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
}

kotlin {

    jvm("desktop")

    sourceSets {
        commonMain.dependencies { implementation(compose.desktop.common) }

        val desktopMain by getting {
            dependencies {
                implementation(projects.core.di)
                implementation(projects.core.datasource.api)
                implementation(projects.core.designsystem)
                implementation(projects.feature.map)
                // from Composite Gradle Build. See root setting.gradle.kts
                //noinspection UseTomlInstead
                implementation("ca.derekellis.mapbox:compose-mapbox-library") // TODO For testing
                implementation(libs.koin.core)
                implementation(libs.koin.mp.compose)
                implementation(compose.desktop.currentOs)

                implementation("org.jetbrains.skiko:skiko-awt-runtime-macos-arm64:0.7.96")
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