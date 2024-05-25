package com.test.app.convention.core

import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project

internal fun Project.configureDetekt(
    commonExtension: DetektExtension,
) {
    commonExtension.apply {
        this.config.setFrom(files(file("$rootDir/tools/detekt/config.yml")))

        reports {
            html.required.set(true)
            html.outputLocation.set(file("build/reports/detekt/detekt.html"))
        }
    }
}
