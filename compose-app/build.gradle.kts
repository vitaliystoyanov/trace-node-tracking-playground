import java.net.Inet4Address
import java.net.NetworkInterface

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "io.architecture.playground"
    compileSdk = libs.versions.compileSdk.get().toInt()

    buildFeatures {
        compose = true
        buildConfig = true
    }

    defaultConfig {
        applicationId = "io.architecture.playground"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.tragetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    applicationVariants.all {
        val variantName = name
        sourceSets {
            getByName("main") {
                java.srcDir(File("build/generated/ksp/$variantName/kotlin"))
            }
        }
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
        jvmTarget = libs.versions.jvmTarget.get()
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlinCompilerExtensionVersion.get()
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
    implementation(projects.core.runtime.configuration)

    implementation(libs.lifecycle.android)
    implementation(libs.androidx.multidex)
    implementation(libs.googleMaterialDesign)

    // Lifecycles
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.lifecycleKtx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.service)
    implementation(libs.androidx.activityCompose)
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.coreKtx)

    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.annotations)
    implementation(libs.koin.compose)
    ksp(libs.koin.ksp.compiler)
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
