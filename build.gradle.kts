plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.publish) apply false
    alias(libs.plugins.dokka)
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
