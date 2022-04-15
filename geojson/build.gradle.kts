plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.dokka)
    alias(libs.plugins.publish)
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
    ios("ios")

    sourceSets["commonMain"].dependencies {
        api(libs.kotlinx.serialization)
    }

    sourceSets["commonTest"].dependencies {
        implementation(kotlin("test"))
        implementation(kotlin("test-annotations-common"))
    }

    sourceSets["jvmMain"].dependencies {
    }

    sourceSets["jvmTest"].dependencies {
    }

    sourceSets["jsMain"].dependencies {
    }

    sourceSets["jsTest"].dependencies {
    }

    sourceSets["nativeMain"].dependencies {}
    sourceSets["nativeTest"].dependencies {}

    sourceSets {
        all {
            with(languageSettings) {
                optIn("kotlin.RequiresOptIn")
                optIn("kotlin.js.ExperimentalJsExport")
                optIn("kotlinx.serialization.InternalSerializationApi")
                optIn("kotlinx.serialization.ExperimentalSerializationApi")
            }
        }

        val nativeMain by getting {}
        getByName("macosMain").dependsOn(nativeMain)
        getByName("iosMain").dependsOn(nativeMain)
        getByName("mingwMain").dependsOn(nativeMain)

        val nativeTest by getting {}
        getByName("macosTest").dependsOn(nativeTest)
        getByName("iosTest").dependsOn(nativeTest)
        getByName("mingwTest").dependsOn(nativeTest)
    }
}

tasks.withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
    // custom output directory
    outputDirectory.set(buildDir.resolve("$rootDir/docs/api"))
}
