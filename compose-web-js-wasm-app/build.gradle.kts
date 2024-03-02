import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin
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

//var version = "0.0.0-SNAPSHOT"
//if (project.hasProperty("skiko.version")) {
//    version = project.properties["skiko.version"] as String
//}
val resourcesDir = "${layout.buildDirectory}buildDir/resources/"

val skikoWasm by configurations.creating

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.buildKonfig)
    alias(libs.plugins.jetbrainsCompose)
}

dependencies {
    skikoWasm(libs.skiko.js.wasm.runtime)
}

val unzipTask = tasks.register("unzipWasm", Copy::class) {
    destinationDir = file(resourcesDir)
    from(skikoWasm.map { zipTree(it) })
}

tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile>().configureEach {
    dependsOn(unzipTask)
}

kotlin {
    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.html.core)
                implementation(compose.material3)
                implementation(libs.skiko)
            }
        }
        val jsWasmMain by creating {
            dependsOn(commonMain)
            resources.setSrcDirs(resources.srcDirs)
            resources.srcDirs(unzipTask.map { it.destinationDir })
        }
        jsMain.dependencies {
            implementation(projects.core.di)
            implementation(projects.core.datasource.api)
            implementation(projects.feature.map)

            // from Composite Gradle Build. See root setting.gradle.kts
            //noinspection UseTomlInstead
            implementation("ca.derekellis.mapbox:compose-mapbox-library")

            implementation(libs.koin.core)
            implementation(libs.koin.mp.compose)

        }
    }
}

compose.experimental {
    web.application {}
}

rootProject.plugins.withType<NodeJsRootPlugin> {
    rootProject.the<NodeJsRootExtension>().nodeVersion = "16.0.0"
}

val fileMapBoxProperties = "mapbox.properties"
val mapBoxPropertyKey = "MAPBOX_ACCESS_TOKEN"

buildkonfig {
    packageName = "io.architecture.compose.web.application"
    defaultConfigs {
        val file = rootProject.file(fileMapBoxProperties)
        val properties = Properties().apply {
            if (file.exists()) {
                load(file.reader())
            }
        }

        val accessToken: String? =
            System.getenv()[mapBoxPropertyKey] ?: properties[mapBoxPropertyKey]?.toString()
        checkNotNull(accessToken) { "'$mapBoxPropertyKey' not defined in $fileMapBoxProperties" }
        buildConfigField(FieldSpec.Type.STRING, mapBoxPropertyKey, accessToken)
    }
}