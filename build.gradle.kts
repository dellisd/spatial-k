import io.gitlab.arturbosch.detekt.Detekt

plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.publish) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.dokka)
}

repositories {
    mavenCentral()
    google()
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}

detekt {
    buildUponDefaultConfig = true
    reports {
        html.enabled = true
    }

    input = files(rootProject.projectDir)
}

tasks.withType<Detekt> {
    jvmTarget = "11"
}

tasks.dokkaGfmMultiModule.configure {
    outputDirectory.set(rootDir.absoluteFile.resolve("docs/api"))
}
