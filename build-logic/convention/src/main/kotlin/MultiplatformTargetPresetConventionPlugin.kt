import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

class MultiplatformTargetPresetConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
            }
            extensions.configure<KotlinMultiplatformExtension> {
                applyDefaultHierarchyTemplate()
                @OptIn(ExperimentalWasmDsl::class)
                wasmJs { browser() }
                js(IR) { browser() }
                androidTarget()
                iosX64()
//                iosArm64()
//                iosSimulatorArm64()
                jvm()
            }
        }
    }
}