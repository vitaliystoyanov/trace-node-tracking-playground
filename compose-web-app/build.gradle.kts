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
        commonMain.dependencies {
            implementation(projects.shared)
        }
        jsMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.html.core)
        }
    }
}

compose.experimental {
    web.application {}
}