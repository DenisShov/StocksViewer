import com.android.build.gradle.LibraryExtension
import com.test.app.convention.core.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("stockviewer.android.library")
                apply("stockviewer.android.library.compose")
                apply("stockviewer.android.library.jacoco")
            }

            extensions.configure<LibraryExtension> {
                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
                testOptions.animationsDisabled = true
            }

            dependencies {
                add("implementation", project(":core:designsystem"))

                add("implementation", libs.findLibrary("androidx.navigation3.runtime").get())
                add("implementation", libs.findLibrary("androidx.navigation3.ui").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.viewmodel.navigation3").get())
                add("implementation", libs.findLibrary("androidx.hilt.lifecycle.viewmodel.compose").get())

                add("implementation", libs.findLibrary("kotlinx.serialization.core").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.viewModelCompose").get())

                add("androidTestImplementation", libs.findLibrary("androidx.lifecycle.runtimeTesting").get())
            }
        }
    }
}
