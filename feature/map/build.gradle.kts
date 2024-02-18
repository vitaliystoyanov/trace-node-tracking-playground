@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "io.architecture.map"
    compileSdk = 34

    buildFeatures {
        compose = true
    }

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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.common) // TODO
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.core.designsystem)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.androidx.animation.core.android)
    implementation(libs.androidx.activityCompose)
    implementation(libs.googleMaterialDesign)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.compose.uiToolingPreview)
    implementation(libs.compose.uiTooling)
    implementation(libs.lifecycle.android)
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.lifecycleKtx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.service)

    // Mapbox Maps SDK
    implementation(libs.android)
    // Note that Compose extension is compatible with Maps SDK v11.0+.
    implementation(libs.maps.compose)
    implementation(libs.maps.style)
    implementation(libs.mapbox.sdk.turf)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.hilt.android.compiler)

    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.appCompat)
    implementation(libs.googleMaterialDesign)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.material3.android)

    testImplementation(libs.junit.junit)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
}