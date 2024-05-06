import io.gitlab.arturbosch.detekt.Detekt
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.publish) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.dokka)
    alias(libs.plugins.ktlint)
}

subprojects {
    apply(plugin = rootProject.libs.plugins.ktlint.get().pluginId)
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        reporters {
            reporter(ReporterType.PLAIN)
            reporter(ReporterType.CHECKSTYLE)
        }
        filter {
            exclude("*.kts")
        }

    }
}

configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    filter {
        exclude("*.kts")
    }
}

detekt {
    buildUponDefaultConfig = true

    source = files(
        project(":geojson").projectDir.resolve("src"),
        project(":turf").projectDir.resolve("src")
    )
}


tasks.withType<Detekt> {
    buildUponDefaultConfig = true
    jvmTarget = "1.8"
    reports {
        html.required.set(true)
    }
}

tasks.dokkaHtmlMultiModule.configure {
    outputDirectory.set(rootDir.absoluteFile.resolve("docs/api"))
    moduleName.set("Spatial K")

    pluginsMapConfiguration.set(
        mapOf(
            "org.jetbrains.dokka.base.DokkaBase" to """
            {
                "footerMessage": "Copyright &copy; 2022 Derek Ellis",
                "customStyleSheets": ["${file("docs/css/logo-styles.css").invariantSeparatorsPath}"]
            }
        """.trimIndent()
        )
    )
}
