plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose)
}

group = "ca.derekellis.mapbox"
version = "0.1.0-SNAPSHOT"

kotlin {
    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
        }
    }
    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
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
