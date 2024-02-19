import java.net.Inet4Address
import java.net.NetworkInterface

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
    alias(libs.plugins.hilt.android)
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

    implementation(libs.lifecycle.android)
    implementation(libs.androidx.multidex)
    implementation (libs.googleMaterialDesign)

    // Lifecycles
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.lifecycleKtx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.service)
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.coreKtx)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)

    kapt(libs.hilt.android.compiler)
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
