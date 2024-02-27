import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "compose-web-app"
        binaries.executable()
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    // Uncomment and configure this if you want to open a browser different from the system default
                    // open = mapOf(
                    //     "app" to mapOf(
                    //         "name" to "google chrome"
                    //     )
                    // )
                }
            }
        }

        // Uncomment the next line to apply Binaryen and get optimized wasm binaries
        // applyBinaryen()
    }

    sourceSets {
        val wasmJsTest by getting
        val wasmJsMain by getting {
            dependencies {
                implementation(projects.core.di)
                implementation(projects.core.datasource.api)
                implementation(projects.feature.map)

                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.ui)
                @OptIn(ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)


                // Koin
                implementation(libs.koin.mp.compose)
            }
        }
    }
}

compose.experimental {
    web.application {}
}