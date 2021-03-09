@file:Suppress("ClassName", "ClassNaming", "Filename", "MatchingDeclarationName")

object deps {
    object plugins {
        const val publish = "com.vanniktech:gradle-maven-publish-plugin:0.14.2"
        const val detekt = "io.gitlab.arturbosch.detekt"
        const val dokka = "org.jetbrains.dokka"
    }

    object kotlinx {
        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.serialization}"
    }
}
