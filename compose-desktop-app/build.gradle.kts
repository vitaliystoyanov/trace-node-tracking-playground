import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
}

kotlin {

    jvm("desktop")

    sourceSets {
        val desktopMain by getting
        desktopMain.dependencies {
            implementation(projects.composeCommon)
            implementation(compose.desktop.currentOs)
            implementation(projects.shared)

            // Koin
            implementation(libs.koin.core.coroutine)
            implementation(libs.koin.mp.compose)
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