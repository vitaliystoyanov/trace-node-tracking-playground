import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import java.util.Properties

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")
        classpath("com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:${libs.versions.buildKonfig.get()}")
    }
}

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.buildKonfig)
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
        val commonMain by getting
        jsMain.dependencies {
            implementation(projects.core.di)
            implementation(projects.core.datasource.api)
            implementation(projects.feature.map)

            // from Composite Gradle Build. See root setting.gradle.kts
            //noinspection UseTomlInstead
            implementation("ca.derekellis.mapbox:compose-mapbox-library")

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

val fileMapBoxProperties = "mapbox.properties"

buildkonfig {
    packageName = "io.architecture.compose.web.app"
    defaultConfigs {
        val file = rootProject.file(fileMapBoxProperties)
        val properties = Properties().apply {
            if (file.exists()) {
                load(file.reader())
            }
        }

        val accessToken: String? = System.getenv()["MAPBOX_ACCESS_TOKEN"] ?: properties["MAPBOX_ACCESS_TOKEN"]?.toString()
        checkNotNull(accessToken) { "'MAPBOX_ACCESS_TOKEN' not defined in $fileMapBoxProperties" }
        buildConfigField(STRING, "MAPBOX_ACCESS_TOKEN", accessToken)
    }
}