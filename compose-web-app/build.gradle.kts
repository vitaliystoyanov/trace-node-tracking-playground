plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
}

kotlin {
    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
            binaries.executable()
        }
    }

    sourceSets {
        jsMain.dependencies {
            implementation(projects.core.di)
            implementation(projects.core.datasource.api)
            implementation(projects.feature.map)

            implementation(libs.koin.core)
            implementation(libs.koin.mp.compose)
            implementation(compose.runtime)
            implementation(compose.html.core)
        }
    }
}

compose.experimental {
    web.application {}
}