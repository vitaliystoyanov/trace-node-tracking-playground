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
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(projects.core.domain)
            implementation(projects.core.data)
            implementation(projects.core.network.websocket.imp.ktor)
            implementation(projects.core.runtime.configuration)

            implementation(libs.kotlinx.coroutine.core)
            implementation(libs.koin.core)
        }
        commonTest.dependencies {
            implementation(projects.core.datasource.api)

            implementation(kotlin("reflect"))
            implementation(libs.ktor.client.core)
            implementation(libs.koin.test)
            implementation(libs.junit.junit)
        }
    }
}