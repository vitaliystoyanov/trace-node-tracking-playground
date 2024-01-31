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
    implementation(libs.compose.material)
    implementation(libs.androidx.activityCompose)
    implementation(libs.googleMaterialDesign)
    implementation(libs.compose.uiToolingPreview)
    implementation(libs.compose.uiTooling)

    implementation(libs.squareRetrofit)
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.multidex)
    androidTestUtil(libs.androidx.orchestrator)
    androidTestImplementation(libs.bundles.base.dependenciesAndroidTests)
    androidTestImplementation(libs.androidx.testJUnit)
    androidTestImplementation(libs.androidx.uiAutomator)
    testImplementation(libs.junit)

    val room_version = "2.6.1"

    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$room_version")

    //  Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")

    // Hilt
    implementation(libs.androidx.hilt.navigation.compose)
    implementation("com.google.dagger:hilt-android:2.46")
    kapt("com.google.dagger:hilt-android-compiler:2.46")
    kapt("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.5.0")

    // Mapbox Maps SDK
    implementation("com.mapbox.maps:android:11.1.0")
    // Note that Compose extension is compatible with Maps SDK v11.0+.
    implementation("com.mapbox.extension:maps-compose:0.1.0")
    implementation("com.mapbox.extension:maps-style:11.1.0")

    // scarlet
    implementation("com.tinder.scarlet:scarlet:0.1.12")
    implementation("com.tinder.scarlet:lifecycle-android:0.1.12")
    implementation("com.tinder.scarlet:websocket-okhttp:0.1.12")
    implementation("com.tinder.scarlet:message-adapter-gson:0.1.12")
    implementation("com.tinder.scarlet:stream-adapter-coroutines:0.1.12")

    // okhttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
}