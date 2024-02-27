plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.convention.multiplatform.target.default)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.common)
                implementation(projects.core.database.api)
                implementation(projects.core.datasource.api)
                implementation(projects.core.model)
                implementation(projects.core.network.websocket.api)
                implementation(projects.core.runtime.logging)

                implementation(libs.koin.core)
                implementation(libs.kotlinx.coroutine.core)
                implementation(libs.kotlinx.datetime)
            }
        }
    }
}