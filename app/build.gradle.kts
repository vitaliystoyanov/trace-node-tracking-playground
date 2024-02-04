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
    }

    defaultConfig {
        applicationId = "io.architecture.playground"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
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

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.androidx.activityCompose)
    implementation(libs.googleMaterialDesign)
    implementation(libs.compose.uiToolingPreview)
    implementation(libs.compose.uiTooling)

    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.lifecycleKtx)

    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.androidx.lifecycle.service)

    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.coreKtx)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.material3)
    annotationProcessor(libs.androidx.room.compiler)
    //noinspection KaptUsageInsteadOfKsp
    kapt(libs.androidx.room.room.compiler)

    //  Kotlin Extensions and Coroutines support for Room
    implementation(libs.androidx.room.ktx)

    // Hilt
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    kapt(libs.kotlinx.metadata.jvm)

    // Mapbox Maps SDK
    implementation(libs.android)
    // Note that Compose extension is compatible with Maps SDK v11.0+.
    implementation(libs.maps.compose)
    implementation(libs.maps.style)
    implementation(libs.mapbox.sdk.turf)

    // scarlet
    implementation(libs.scarlet)
    implementation(libs.lifecycle.android)
    implementation(libs.websocket.okhttp)
    implementation(libs.message.adapter.gson)
    implementation(libs.stream.adapter.coroutines)

    // okhttp
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

}