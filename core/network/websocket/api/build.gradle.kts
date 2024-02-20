@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.kotlin.serialize)
}

dependencies {
    implementation(projects.core.model)
    implementation(libs.gson)

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3") // Doesn't have support of Kotlin multiplatform / jvm only

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.protobuf)
}