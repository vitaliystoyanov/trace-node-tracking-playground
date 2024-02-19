@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialize)
}

android {
    namespace = "io.architecture.scarlet"
    compileSdk = 34

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(projects.core.network.websocket.api)
    implementation(projects.core.datasource.api)
    implementation(projects.core.model)

    // scarlet
    implementation(libs.scarlet)
    implementation(libs.websocket.okhttp)
    implementation(libs.message.adapter.gson)
    implementation(libs.message.adapter.protobuf)
    implementation(libs.stream.adapter.coroutines)

    // kotlinx-serialization
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.protobuf)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.android.compiler)

    // okhttp
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    testImplementation(libs.junit.junit)
    androidTestImplementation(libs.junit)
}