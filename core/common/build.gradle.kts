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
         commonMain.dependencies {
             implementation(libs.koin.core)
             implementation(libs.kotlinx.coroutine.core)
             implementation(projects.core.runtime.logging)
        }
        androidMain.dependencies {
//             https://github.com/Kotlin/kotlinx.coroutines?tab=readme-ov-file#multiplatform
//            implementation(libs.kotlinx.coroutine.android)
        }
    }
}