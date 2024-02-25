import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.LibraryExtension
import io.architecture.build.logic.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
            }
            extensions.configure<LibraryExtension> {
                namespace = "io.architecture.playground" + path.replace(":", ".")

                compileSdk = libs.findVersion("compileSdk").get().requiredVersion.toInt()
                defaultConfig.targetSdk =
                    libs.findVersion("tragetSdk").get().requiredVersion.toInt()

                compileOptions.sourceCompatibility = JavaVersion.VERSION_17
                compileOptions.targetCompatibility = JavaVersion.VERSION_17
            }
            extensions.configure<LibraryAndroidComponentsExtension> {

            }
            dependencies {
                add("testImplementation", kotlin("test"))
            }
        }
    }
}
