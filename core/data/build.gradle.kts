import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.convention.android.library)
}

kotlin {
    // Apply the default hierarchy again
    applyDefaultHierarchyTemplate()
//    @OptIn(ExperimentalWasmDsl::class)
//    wasmJs {
//        browser()
//    }
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.common)
                implementation(projects.core.database.api)
                implementation(projects.core.datasource.api)
                implementation(projects.core.model)
                implementation(projects.core.network.websocket.api)
                implementation(projects.core.runtime.logging)

                implementation(libs.koin.core)
                implementation(libs.kotlinx.coroutine.core)
                implementation(libs.kotlinx.datetime)
            }
        }
    }
}