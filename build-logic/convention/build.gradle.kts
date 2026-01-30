plugins {
    `kotlin-dsl`
}

group = "com.test.app.stockviewer.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.detekt.gradlePlugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {

        register("androidHilt") {
            id = "stockviewer.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }

        register("androidApplication") {
            id = "stockviewer.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }

        register("androidApplicationCompose") {
            id = "stockviewer.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }

        register("androidApplicationJacoco") {
            id = "stockviewer.android.application.jacoco"
            implementationClass = "AndroidApplicationJacocoConventionPlugin"
        }

        register("androidApplicationDetekt") {
            id = "stockviewer.android.application.detekt"
            implementationClass = "AndroidApplicationDeteKtConventionPlugin"
        }

        register("androidFeature") {
            id = "stockviewer.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }

        register("androidLibrary") {
            id = "stockviewer.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }

        register("androidLibraryCompose") {
            id = "stockviewer.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }

        register("androidLibraryJacoco") {
            id = "stockviewer.android.library.jacoco"
            implementationClass = "AndroidLibraryJacocoConventionPlugin"
        }

        register("jvmLibrary") {
            id = "stockviewer.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }

    }
}
