pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven(url = "https://dl.bintray.com/kotlin/kotlin-eap")
    }
}

rootProject.name = "spatial-k"

include("geojson", "geojson-dsl", "turf")
