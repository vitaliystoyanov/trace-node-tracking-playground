@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "io.architecture.core.di"
    compileSdk = libs.versions.compileSdk.get().toInt()

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    libraryVariants.all {
        val variantName = name
        sourceSets {
            getByName("main") {
                java.srcDir(File("build/generated/ksp/$variantName/kotlin"))
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
}
dependencies {
    implementation(projects.core.common)
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.core.network.websocket.imp.ktor)
    implementation(projects.core.database.imp.room)
    implementation(projects.core.runtime.configuration)
    implementation(projects.feature.map)

    implementation(libs.koin.core)
    implementation(libs.koin.annotations)
    implementation(libs.koin.test)
    implementation(libs.koin.android.test)
    ksp(libs.koin.ksp.compiler)

    testImplementation(libs.ktor.client.core)
    testImplementation(libs.ktor.client.cio)
    testImplementation(libs.ktor.client.websockets)
    testImplementation(libs.ktor.client.serialization.jvm)
    testImplementation(libs.ktor.serialization.kotlinx.protobuf)
    testImplementation(libs.ktor.client.logging.jvm)

    testImplementation(libs.junit.junit)
}