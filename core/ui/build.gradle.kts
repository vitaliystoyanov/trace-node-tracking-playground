import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.convention.multiplatform.target.default)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.designsystem)
            implementation(projects.core.model)

            implementation(libs.koin.core)
            implementation(libs.koin.mp.compose)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
        }
    }
}