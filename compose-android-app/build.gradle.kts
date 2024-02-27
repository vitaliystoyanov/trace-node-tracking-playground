import java.net.Inet4Address
import java.net.NetworkInterface

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    androidTarget()
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core.coroutine)
        }
        androidMain.dependencies {
            implementation(projects.core.database.imp.room)
            implementation(projects.core.di)
            implementation(projects.core.domain)
            implementation(projects.feature.map)

            implementation(libs.koin.android)
            implementation(libs.koin.compose)
            implementation(libs.kotlinx.coroutine.android)

            implementation(libs.scarlet.lifecycle.android)
            implementation(libs.androidx.multidex)
            implementation(libs.googleMaterialDesign)

            // Lifecycles
            implementation(libs.androidx.lifecycle)
            implementation(libs.androidx.lifecycleKtx)
            implementation(libs.androidx.lifecycle.service)
            implementation(libs.androidx.activityCompose)
            implementation(libs.androidx.appCompat)
            implementation(libs.androidx.coreKtx)
        }
    }
}

android {
    namespace = "io.architecture.playground"
    compileSdk = libs.versions.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlinCompilerExtensionVersion.get()
    }
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
