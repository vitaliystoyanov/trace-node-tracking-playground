import java.net.Inet4Address
import java.net.NetworkInterface

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "io.architecture.playground"
    compileSdk = 34

    buildFeatures {
        compose = true
        buildConfig = true
    }

    defaultConfig {
        applicationId = "io.architecture.playground"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField("String", "DEV_MACHINE_IP", "\"" + getLocalIPv4[0] + "\"")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
    // Allow references to generated code
    kapt {
        correctErrorTypes = true
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.domain)
    implementation(projects.feature.map)
    implementation(projects.core.model)
    implementation(projects.core.database.api)
    implementation(projects.core.datasource.api)
    implementation(projects.core.network.websocket.imp.ktor)
    implementation(projects.core.database.imp.room)

//    implementation(platform(libs.compose.bom))
//    implementation(libs.compose.ui)
//    implementation(libs.androidx.activityCompose)
//    implementation(libs.googleMaterialDesign)
//    implementation(libs.accompanist.systemuicontroller)
//    implementation(libs.compose.uiToolingPreview)
//    implementation(libs.compose.uiTooling)
//    implementation(libs.androidx.runtime.tracing)


    implementation(libs.lifecycle.android)
    implementation("androidx.multidex:multidex:2.0.1")
    // Lifecycles
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.lifecycleKtx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.service)
//
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.coreKtx)
//
//    // kotlinx-serialization
//    implementation(libs.kotlinx.serialization.json)
//    implementation(libs.kotlinx.serialization.protobuf)
//
//    // Ktor for Android
//    implementation(libs.ktor.client.core)
//    implementation(libs.ktor.client.cio)
//    implementation(libs.ktor.client.websockets)
//    implementation(libs.ktor.client.serialization.jvm)
//    implementation(libs.ktor.serialization.kotlinx.protobuf)
//    implementation(libs.ktor.client.logging.jvm)
//
//    // Room
//    implementation(libs.androidx.room.runtime)
//    implementation(libs.androidx.material3)
//    annotationProcessor(libs.androidx.room.compiler)
//    //noinspection KaptUsageInsteadOfKsp
//    kapt(libs.androidx.room.room.compiler)
//
//    //  Kotlin Extensions and Coroutines support for Room
//    implementation(libs.androidx.room.ktx)
//
    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.hilt.android.compiler)
//
//    // Mapbox Maps SDK
//    implementation(libs.android)
//    // Note that Compose extension is compatible with Maps SDK v11.0+.
//    implementation(libs.maps.compose)
//    implementation(libs.maps.style)
//    implementation(libs.mapbox.sdk.turf)
//
//    // scarlet
//    implementation(libs.scarlet)
//    implementation(libs.websocket.okhttp)
//    implementation(libs.message.adapter.gson)
//    implementation(libs.message.adapter.protobuf)
//    implementation(libs.stream.adapter.coroutines)
//
//
//    // okhttp
//    implementation(libs.okhttp)
//    implementation(libs.logging.interceptor)

}

val getLocalIPv4: List<String>
    get() {
        val ip4s = mutableListOf<String>()
        NetworkInterface.getNetworkInterfaces().toList()
            .filter { it.isUp && !it.isLoopback && !it.isVirtual }
            .forEach { networkInterface ->
                networkInterface.inetAddresses.toList()
                    .filter { !it.isLoopbackAddress && it is Inet4Address }
                    .forEach { inetAddress -> ip4s.add(inetAddress.hostAddress) }
            }
        return ip4s
    }
