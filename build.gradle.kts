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

    source = files(
        project(":geojson").projectDir.resolve("src"),
        project(":turf").projectDir.resolve("src")
    )
}


tasks.withType<Detekt> {
    buildUponDefaultConfig = true
    jvmTarget = "11"
    reports {
        html.required.set(true)
    }
}

tasks.dokkaGfmMultiModule.configure {
    outputDirectory.set(rootDir.absoluteFile.resolve("docs/api"))
}
