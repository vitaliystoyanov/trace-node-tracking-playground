plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    jvm()

    sourceSets {
        commonMain.dependencies {
            // put your Multiplatform dependencies here
        }
    }
}

android {
    namespace = "io.architecture.playground.shared"
    compileSdk = 34
    defaultConfig {
        minSdk = 23
    }
}

dependencies {
//    // kotlinx-serialization
//    implementation(libs.kotlinx.serialization.json)
//    implementation(libs.kotlinx.serialization.protobuf)
//
//    // GSON
//    implementation(libs.gson)
}
