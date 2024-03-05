plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.kotlin.serialize)
    alias(libs.plugins.convention.multiplatform.target.default)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.model)
            implementation(libs.gson)

            implementation(libs.kotlinx.coroutine.core)

            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.serialization.protobuf)
        }
    }
}