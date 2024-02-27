import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.convention.android.library)
}

kotlin {
    // Apply the default hierarchy again
    applyDefaultHierarchyTemplate()
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }
    js(IR) {
        browser()
    }
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.data)
            implementation(projects.core.common)
            implementation(projects.core.model)
            implementation(projects.core.runtime.logging)

            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.coroutine.core)
            implementation(libs.koin.core)
        }
    }
}