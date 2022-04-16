import java.net.URL

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.dokka)
    alias(libs.plugins.publish)
    alias(libs.plugins.kotlinx.benchmark)
}

kotlin {
    jvm {
        compilations.create("bench")
    }
    js {
        browser {
        }
        nodejs {
        }

        compilations.create("bench")
    }
    // For ARM, should be changed to iosArm32 or iosArm64
    // For Linux, should be changed to e.g. linuxX64
    // For MacOS, should be changed to e.g. macosX64
    // For Windows, should be changed to e.g. mingwX64
    linuxX64("native") {
        compilations.create("bench")
    }
    mingwX64("mingw")
    macosX64("macos")
    ios("ios")

    sourceSets {
        all {
            with(languageSettings) {
                optIn("kotlin.RequiresOptIn")
                optIn("kotlin.js.ExperimentalJsExport")
                optIn("kotlinx.serialization.InternalSerializationApi")
                optIn("kotlinx.serialization.ExperimentalSerializationApi")
            }
        }

        val commonMain by getting {
            dependencies {
                api(libs.kotlinx.serialization)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jsMain by getting {}

        val jvmMain by getting {}

        val nativeMain by getting {
            getByName("macosMain").dependsOn(this)
            getByName("iosMain").dependsOn(this)
            getByName("mingwMain").dependsOn(this)
        }

        val nativeTest by getting {
            getByName("macosTest").dependsOn(this)
            getByName("iosTest").dependsOn(this)
            getByName("mingwTest").dependsOn(this)
        }

        val commonBench by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.kotlinx.benchmark)
            }
        }

        val jsBench by getting {
            dependsOn(commonBench)
            dependsOn(jsMain)
        }

        val jvmBench by getting {
            dependsOn(commonBench)
            dependsOn(jvmMain)
        }

        val nativeBench by getting {
            dependsOn(commonBench)
            dependsOn(nativeMain)
        }
    }
}

benchmark {
    this.configurations {
        getByName("main") {
            iterations = 5
        }
    }

    targets {
        register("jvmBench")
        // Broken on 1.6.20???
        register("jsBench")
        register("nativeBench")
    }
}

tasks.withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
    // custom output directory
    outputDirectory.set(buildDir.resolve("$rootDir/docs/api"))
}
