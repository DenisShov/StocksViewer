//import com.test.app.convention.core.configureKtLint
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
//import org.jlleitschuh.gradle.ktlint.KtlintExtension

class AndroidApplicationKtLintConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jlleitschuh.gradle.ktlint")

//            val extension = extensions.getByType<KtlintExtension>()
//            configureKtLint(extension)
        }
    }

}