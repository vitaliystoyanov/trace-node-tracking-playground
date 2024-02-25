@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "io.architecture.core.design.system"
    compileSdk = libs.versions.compileSdk.get().toInt()


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
}

dependencies {
    implementation(libs.androidx.coreKtx)
    testImplementation(libs.junit.junit)
    implementation(libs.androidx.ui.graphics.android)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
}