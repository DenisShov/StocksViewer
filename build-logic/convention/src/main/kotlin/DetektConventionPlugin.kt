import com.test.app.stockviewer.configureDetekt
import com.test.app.stockviewer.libs
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class DetektConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("io.gitlab.arturbosch.detekt")

            val extension = extensions.getByType<DetektExtension>()
            configureDetekt(extension)

            // Adds a formatting plugin
            dependencies.apply {
                add("detektPlugins", libs.findLibrary("detekt-formatting").get())
                add("detektPlugins", libs.findLibrary("detekt-libraries").get())
            }
        }
    }

}
