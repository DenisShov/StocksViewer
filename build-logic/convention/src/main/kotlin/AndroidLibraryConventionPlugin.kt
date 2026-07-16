import com.android.build.api.dsl.LibraryExtension
import com.test.app.stockviewer.configureKotlinAndroid
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
                apply("stockviewer.detekt")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
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
                if (path != ":core:commonresources") {
                    add("implementation", project(":core:commonresources"))
                    add("androidTestImplementation", project(":core:commonresources"))
                }
                add("testImplementation", kotlin("test"))
            }
        }
    }
}