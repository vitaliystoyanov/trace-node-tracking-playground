import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.compose)
}

group = "ca.derekellis.mapbox"
version = "11.1.0-SNAPSHOT" // corresponding to MapBox SDK version

kotlin {
    applyDefaultHierarchyTemplate()
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs { browser() }
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    jvm()
    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
        }
    }

    cocoapods {
        summary = "Compose MapBox Library"
        homepage = "TBR"
        version = "11.1.0"
        ios.deploymentTarget = "16.0"
        pod("MapboxMaps") {
            version = "11.1.0"
            packageName = "MapBox"
            // https://kotlinlang.org/docs/native-cocoapods-libraries.html#support-for-objective-c-headers-with-import-directives
            extraOpts += listOf("-compiler-option", "-fmodules") // TODO look up in docs what it's
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(compose.html.core)
                implementation(compose.runtime)
                implementation(libs.kotlinx.coroutine.core)
                implementation(npm("mapbox-gl", libs.versions.mapbox.get()))
            }
        }
        val jsTest by getting
    }
}

android {
    namespace = "io.architecture.mapbox.compose.library"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

compose {
    // https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-compatibility-and-versioning.html#use-a-developer-version-of-compose-multiplatform-compiler
    kotlinCompilerPlugin.set("1.5.4")
}