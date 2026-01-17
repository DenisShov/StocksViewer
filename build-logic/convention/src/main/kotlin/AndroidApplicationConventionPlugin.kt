import com.android.build.api.dsl.ApplicationExtension
import com.test.app.convention.core.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("codewars.android.application.detekt")
            }

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 36

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
                testOptions {
                    unitTests {
                        isIncludeAndroidResources = true
                    }
                }
            }
            dependencies {
                add("implementation", project(":core:commonresources"))
                add("androidTestImplementation", project(":core:commonresources"))
            }
        }
    }

}
