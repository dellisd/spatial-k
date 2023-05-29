import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.dokka)
    alias(libs.plugins.publish)
    alias(libs.plugins.resources)
}

kotlin {
    jvm()
    js {
        browser {
        }
        nodejs {
        }
    }
    // For ARM, should be changed to iosArm32 or iosArm64
    // For Linux, should be changed to e.g. linuxX64
    // For MacOS, should be changed to e.g. macosX64
    // For Windows, should be changed to e.g. mingwX64

    linuxX64("native")
    mingwX64("mingw")
    macosX64("macos")
    iosArm64()
    iosX64()
    iosSimulatorArm64()

    sourceSets["commonMain"].dependencies {
        api(project(":geojson"))
    }

    sourceSets["commonTest"].dependencies {
        implementation(kotlin("test"))
        implementation(kotlin("test-annotations-common"))
        implementation(libs.resources)
    }

    sourceSets["jvmMain"].dependencies {
    }

    sourceSets["jvmTest"].dependencies {
    }

    sourceSets["jsMain"].dependencies {
    }

    sourceSets["jsTest"].dependencies {
    }

    sourceSets["nativeMain"].dependencies {
    }
    sourceSets["nativeTest"].dependencies {
    }

    sourceSets {
        val nativeMain by getting {}
        getByName("macosMain").dependsOn(nativeMain)
        getByName("mingwMain").dependsOn(nativeMain)
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(nativeMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }

        val nativeTest by getting {}
        getByName("macosTest").dependsOn(nativeTest)
        getByName("mingwTest").dependsOn(nativeTest)
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(nativeTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }

        all {
            with(languageSettings) {
                optIn("kotlin.RequiresOptIn")
            }
        }
    }
}

tasks.named("jsBrowserTest") { enabled = false }

tasks.withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
    // custom output directory
    outputDirectory.set(buildDir.resolve("$rootDir/docs/api"))
}

tasks.withType(KotlinCompile::class.java).configureEach {
    compilerOptions.jvmTarget.set(JvmTarget.JVM_1_8)
}

// Working around dokka problems
afterEvaluate {
    tasks.named("dokkaJavadocJar").configure {
        dependsOn(":geojson:dokkaHtml")
    }
}
