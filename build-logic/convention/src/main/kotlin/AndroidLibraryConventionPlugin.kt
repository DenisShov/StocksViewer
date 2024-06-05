import com.android.build.gradle.LibraryExtension
import com.test.app.convention.core.configureKotlinAndroid
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
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 34
                packaging {
                    resources {
                        excludes.addAll(
                            listOf(
                                "/META-INF/{AL2.0,LGPL2.1}",
                                "META-INF/LICENSE.md",
                                "META-INF/LICENSE-notice.md",
                            )
                        )
                    }
                }
            }
            dependencies {
                add("implementation", project(":core:commonresources"))
                add("androidTestImplementation", project(":core:commonresources"))
                add("testImplementation", kotlin("test"))
            }
        }
    }
}