plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.convention.multiplatform.target.default)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.model)
            implementation(projects.core.database.api)
            implementation(projects.core.network.websocket.api)

            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutine.core)
        }
    }
}