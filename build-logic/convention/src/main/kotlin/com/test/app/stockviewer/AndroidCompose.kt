package com.test.app.stockviewer

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

/**
 * Configure Compose-specific options
 */
internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension,
) {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

    commonExtension.apply {
        buildFeatures.compose = true

        dependencies {
            val bom = libs.findLibrary("androidx-compose-bom").get()
            add("implementation", platform(bom))
            add("androidTestImplementation", platform(bom))
            add("implementation", libs.findLibrary("androidx-compose-ui-tooling-preview").get())
            add("debugImplementation", libs.findLibrary("androidx-compose-ui-tooling").get())
        }
    }

    // Opt-in Compose Compiler reports for stability diagnostics. Triggered by the Gradle
    // property `-PcomposeCompilerReports=true` so default builds stay fast.
    //
    // Usage:
    //   ./gradlew :app:assembleRelease -PcomposeCompilerReports=true
    //   ./gradlew :feature:list:impl:assembleRelease -PcomposeCompilerReports=true
    //
    // Output lands at <module>/build/compose_compiler/.
    extensions.configure<ComposeCompilerGradlePluginExtension> {
        val reportsEnabled = providers.gradleProperty("composeCompilerReports").orNull == "true"
        if (reportsEnabled) {
            val dest = layout.buildDirectory.dir("compose_compiler")
            reportsDestination.set(dest)
            metricsDestination.set(dest)
        }
    }
}
