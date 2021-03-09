buildscript {
    repositories {
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath(deps.plugins.publish)
    }
}

plugins {
    id("org.jetbrains.kotlin.multiplatform") version Versions.kotlin apply false
    id("org.jetbrains.kotlin.plugin.serialization") version Versions.kotlin apply false
    id("io.gitlab.arturbosch.detekt") version Versions.detekt
    id("org.jetbrains.dokka") version Versions.dokka
}

repositories {
    mavenCentral()
    jcenter()
    jcenter {
        content {
            // just allow to include kotlinx projects
            // detekt needs "kotlinx-html" for the html report
            includeGroup("org.jetbrains.kotlinx")
        }
    }
    maven(url = "https://dl.bintray.com/kotlin/kotlin-eap")
}

subprojects {
    group = "io.github.dellisd.spatialk"
    version = "0.0.2"
}

allprojects {
    repositories {
        mavenCentral()
        jcenter()
        maven(url = "https://dl.bintray.com/kotlin/kotlin-eap" )
        maven(url = "https://kotlin.bintray.com/kotlinx" )
    }
}

detekt {
    failFast = false
    buildUponDefaultConfig = true
    reports {
        html.enabled = true
    }

    input = files(rootProject.projectDir)
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt> {
    jvmTarget = "11"
}

tasks.dokkaGfmMultiModule.configure {

}
