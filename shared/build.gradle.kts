import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.jetbrainsCompose)
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

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    jvm()

    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(projects.core.database.imp.room)

                api(projects.feature.map)
            }
        }
        val commonMain by getting {
            dependencies {
                api(projects.core.di)
                // Used to mock LocalDatasource temporary
                implementation(projects.core.datasource.api)
                implementation(projects.core.database.api)

                implementation(projects.core.ui) // TODO for tests

                implementation(libs.koin.core)
                implementation(libs.koin.mp.compose)

                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.ui)
                @OptIn(ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(libs.koin.mp.compose)
            }
        }
        val jvmMain by getting {
            dependencies {
            }
        }
        //        val wasmJsMain by getting {
//            dependencies {
//
//            }
//        }
        val jsMain by getting
        val jsTest by getting
    }
}