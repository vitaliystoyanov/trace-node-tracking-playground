plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
}

kotlin {
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        iosMain.dependencies {
            implementation(projects.core.di)
            implementation(projects.core.datasource.api)
            implementation(projects.feature.map)

            implementation(libs.koin.core)
            implementation(libs.koin.mp.compose)
        }
    }
}